package com.intuso.housemate.client.proxy.api.internal.object;

import com.google.common.collect.Maps;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Map;

/**
 * @param <VALUE> the type of the value for the enabled status of the command
 * @param <PARAMETERS> the type of the parameters list
 * @param <COMMAND> the type of the command
 */
public abstract class ProxyCommand<
            VALUE extends ProxyValue<?, VALUE>,
            PARAMETERS extends ProxyList<? extends ProxyParameter<?, ?>, ?>,
            COMMAND extends ProxyCommand<VALUE, PARAMETERS, COMMAND>>
        extends ProxyObject<Command.Data, Command.Listener<? super COMMAND>>
        implements Command<Type.InstanceMap, VALUE, PARAMETERS, COMMAND> {

    private final VALUE enabledValue;
    private final PARAMETERS parameters;

    private Session session;
    private JMSUtil.Sender performSender;
    private JMSUtil.Receiver<PerformStatusData> performStatusReceiver;

    private int nextId;
    private final Map<String, Command.PerformListener<? super COMMAND>> listenerMap = Maps.newHashMap();

    /**
     * @param logger {@inheritDoc}
     */
    protected ProxyCommand(Logger logger,
                           ListenersFactory listenersFactory,
                           ProxyObject.Factory<VALUE> valueFactory,
                           ProxyObject.Factory<PARAMETERS> parametersFactory) {
        super(logger, Command.Data.class, listenersFactory);
        enabledValue = valueFactory.create(ChildUtil.logger(logger, Command.ENABLED_ID));
        parameters = parametersFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        enabledValue.init(ChildUtil.name(name, Command.ENABLED_ID), connection);
        parameters.init(ChildUtil.name(name, Command.PARAMETERS_ID), connection);
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        performSender = new JMSUtil.Sender(session, session.createProducer(session.createQueue(ChildUtil.name(name, Command.PERFORM_ID))));
        performStatusReceiver = new JMSUtil.Receiver<>(logger,
                session.createConsumer(session.createTopic(ChildUtil.name(name, Command.PERFORM_STATUS_ID) + "?consumer.retroactive=true")),
                PerformStatusData.class,
                new JMSUtil.Receiver.Listener<PerformStatusData>() {
                    @Override
                    public void onMessage(PerformStatusData performStatusData, boolean wasPersisted) {
                        if (listenerMap.containsKey(performStatusData.getOpId())) {
                            if (performStatusData.isFinished()) {
                                if (performStatusData.getError() == null)
                                    listenerMap.remove(performStatusData.getOpId()).commandFinished(getThis());
                                else
                                    listenerMap.remove(performStatusData.getOpId()).commandFailed(getThis(), performStatusData.getError());
                            } else
                                listenerMap.get(performStatusData.getOpId()).commandStarted(getThis());
                        }
                        // todo call object listeners
                    }
                });
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        enabledValue.uninit();
        parameters.uninit();
        if(performSender != null) {
            try {
                performSender.close();
            } catch(JMSException e) {
                logger.error("Failed to close perform sender");
            }
            performSender = null;
        }
        if(performStatusReceiver != null) {
            try {
                performStatusReceiver.close();
            } catch(JMSException e) {
                logger.error("Failed to close perform status receiver");
            }
            performStatusReceiver = null;
        }
        if(session != null) {
            try {
                session.close();
            } catch(JMSException e) {
                logger.error("Failed to close session");
            }
            session = null;
        }
    }

    public boolean isEnabled() {
        return enabledValue != null
                && enabledValue.getValue() != null
                && enabledValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(enabledValue.getValue().getFirstValue());
    }

    @Override
    public VALUE getEnabledValue() {
        return enabledValue;
    }

    @Override
    public PARAMETERS getParameters() {
        return parameters;
    }

    /**
     * Performs the command without any type values. It is not correct to use this method on a command that has parameters
     * @param listener the listener for progress of the command
     */
    public final void perform(Command.PerformListener<? super COMMAND> listener) {
        perform(new Type.InstanceMap(), listener);
    }

    private COMMAND getThis() {
        return (COMMAND) this;
    }

    @Override
    public final synchronized void perform(Type.InstanceMap values, Command.PerformListener<? super COMMAND> listener) {
        String id = null;
        if(listener != null) {
            id = "" + nextId++;
            listenerMap.put(id, listener);
        }
        try {
            performSender.send(values, true);
        } catch(JMSException e) {
            if(listener != null) {
                listenerMap.remove(id);
                listener.commandFailed(getThis(), "Failed to send perform message: " + e.getMessage());
            }
            throw new HousemateException("Failed to send perform message", e);
        }
    }
}

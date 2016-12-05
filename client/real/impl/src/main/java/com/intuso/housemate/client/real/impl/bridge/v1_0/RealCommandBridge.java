package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.CommandMapper;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.JMSUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Map;

/**
 * Created by tomc on 28/11/16.
 */
public class RealCommandBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.Command.Data, Command.Data, Command.Listener<? super RealCommandBridge>>
        implements Command<Type.InstanceMap, RealValueBridge, RealListBridge<RealParameterBridge>, RealCommandBridge> {

    private final RealValueBridge enabledValue;
    private final RealListBridge<RealParameterBridge> parameters;

    private Session session;
    private com.intuso.housemate.client.v1_0.real.impl.JMSUtil.Sender performSender;
    private JMSUtil.Receiver<Command.PerformData> performReceiver;
    private JMSUtil.Sender performStatusSender;
    private com.intuso.housemate.client.v1_0.real.impl.JMSUtil.Receiver<com.intuso.housemate.client.v1_0.api.object.Command.PerformStatusData> performStatusReceiver;

    private int nextId;
    private final Map<String, Command.PerformListener<? super RealCommandBridge>> listenerMap = Maps.newHashMap();

    @Inject
    protected RealCommandBridge(@Assisted Logger logger,
                                CommandMapper commandMapper,
                                ListenersFactory listenersFactory,
                                RealObjectBridge.Factory<RealValueBridge> valueFactory,
                                RealObjectBridge.Factory<RealListBridge<RealParameterBridge>> parametersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Command.Data.class, commandMapper, listenersFactory);
        enabledValue = valueFactory.create(ChildUtil.logger(logger, Command.ENABLED_ID));
        parameters = parametersFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        enabledValue.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Command.ENABLED_ID),
                ChildUtil.name(internalName, Command.ENABLED_ID),
                connection);
        parameters.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Command.PARAMETERS_ID),
                ChildUtil.name(internalName, Command.PARAMETERS_ID),
                connection);
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        performSender = new com.intuso.housemate.client.v1_0.real.impl.JMSUtil.Sender(session,
                session.createProducer(session.createQueue(com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, Command.PERFORM_ID))));
        performReceiver = new JMSUtil.Receiver<>(logger,
                session.createConsumer(session.createQueue(ChildUtil.name(internalName, Command.PERFORM_ID))),
                PerformData.class,
                new JMSUtil.Receiver.Listener<PerformData>() {
                    @Override
                    public void onMessage(PerformData performData, boolean wasPersisted) {
                        // todo
                    }
                });
        performStatusSender = new JMSUtil.Sender(session,
                session.createProducer(session.createTopic(ChildUtil.name(internalName, Command.PERFORM_STATUS_ID))));
        performStatusReceiver = new com.intuso.housemate.client.v1_0.real.impl.JMSUtil.Receiver<>(logger,
                session.createConsumer(session.createTopic(com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, Command.PERFORM_STATUS_ID) + "?consumer.retroactive=true")),
                com.intuso.housemate.client.v1_0.api.object.Command.PerformStatusData.class,
                new com.intuso.housemate.client.v1_0.real.impl.JMSUtil.Receiver.Listener<com.intuso.housemate.client.v1_0.api.object.Command.PerformStatusData>() {
            @Override
            public void onMessage(com.intuso.housemate.client.v1_0.api.object.Command.PerformStatusData performStatusData, boolean wasPersisted) {
                if (listenerMap.containsKey(performStatusData.getOpId())) {
                    if (performStatusData.isFinished()) {
                        if (performStatusData.getError() == null)
                            listenerMap.remove(performStatusData.getOpId()).commandFinished(RealCommandBridge.this);
                        else
                            listenerMap.remove(performStatusData.getOpId()).commandFailed(RealCommandBridge.this, performStatusData.getError());
                    } else
                        listenerMap.get(performStatusData.getOpId()).commandStarted(RealCommandBridge.this);
                }
                // todo call object listeners
                // todo send status via sender
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
        if(performReceiver != null) {
            try {
                performReceiver.close();
            } catch(JMSException e) {
                logger.error("Failed to close perform receiver");
            }
            performReceiver = null;
        }
        if(performStatusSender != null) {
            try {
                performStatusSender.close();
            } catch(JMSException e) {
                logger.error("Failed to close perform status sender");
            }
            performStatusSender = null;
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
    public RealValueBridge getEnabledValue() {
        return enabledValue;
    }

    @Override
    public RealListBridge<RealParameterBridge> getParameters() {
        return parameters;
    }

    /**
     * Performs the command without any type values. It is not correct to use this method on a command that has parameters
     * @param listener the listener for progress of the command
     */
    public final void perform(Command.PerformListener<? super RealCommandBridge> listener) {
        perform(new Type.InstanceMap(), listener);
    }

    @Override
    public final synchronized void perform(Type.InstanceMap values, Command.PerformListener<? super RealCommandBridge> listener) {
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
                listener.commandFailed(this, "Failed to send perform message: " + e.getMessage());
            }
            throw new HousemateException("Failed to send perform message", e);
        }
    }
}

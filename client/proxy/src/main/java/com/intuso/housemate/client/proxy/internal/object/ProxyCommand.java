package com.intuso.housemate.client.proxy.internal.object;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

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

    private final Sender.Factory senderFactory;

    private final VALUE enabledValue;
    private final PARAMETERS parameters;

    private Sender performSender;
    private Receiver<PerformStatusData> performStatusReceiver;

    private int nextId;
    private final Map<String, Command.PerformListener<? super COMMAND>> listenerMap = Maps.newHashMap();

    /**
     * @param logger {@inheritDoc}
     */
    protected ProxyCommand(Logger logger,
                           ManagedCollectionFactory managedCollectionFactory,
                           Receiver.Factory receiverFactory,
                           Sender.Factory senderFactory,
                           ProxyObject.Factory<VALUE> valueFactory,
                           ProxyObject.Factory<PARAMETERS> parametersFactory) {
        super(logger, Command.Data.class, managedCollectionFactory, receiverFactory);
        this.senderFactory = senderFactory;
        enabledValue = valueFactory.create(ChildUtil.logger(logger, ENABLED_ID));
        parameters = parametersFactory.create(ChildUtil.logger(logger, PARAMETERS_ID));
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        enabledValue.init(ChildUtil.name(name, ENABLED_ID));
        parameters.init(ChildUtil.name(name, PARAMETERS_ID));
        performSender = senderFactory.create(logger, com.intuso.housemate.client.messaging.api.internal.Type.Queue, ChildUtil.name(name, PERFORM_ID));
        performStatusReceiver = receiverFactory.create(logger, com.intuso.housemate.client.messaging.api.internal.Type.Topic, ChildUtil.name(name, PERFORM_STATUS_ID), PerformStatusData.class);
        performStatusReceiver.listen(new Receiver.Listener<PerformStatusData>() {
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
            performSender.close();
            performSender = null;
        }
        if(performStatusReceiver != null) {
            performStatusReceiver.close();
            performStatusReceiver = null;
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
            performSender.send(new PerformData(id, values), true);
        } catch(Throwable t) {
            if(listener != null) {
                listenerMap.remove(id);
                listener.commandFailed(getThis(), "Failed to send perform message: " + t.getMessage());
            }
            throw new HousemateException("Failed to send perform message", t);
        }
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        if(ENABLED_ID.equals(id))
            return enabledValue;
        else if(PARAMETERS_ID.equals(id))
            return parameters;
        return null;
    }

    /**
    * Created with IntelliJ IDEA.
    * User: tomc
    * Date: 14/01/14
    * Time: 13:16
    * To change this template use File | Settings | File Templates.
    */
    public static final class Simple extends ProxyCommand<
            ProxyValue.Simple,
            ProxyList.Simple<ProxyParameter.Simple>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Sender.Factory senderFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyList.Simple<ProxyParameter.Simple>> parametersFactory) {
            super(logger, managedCollectionFactory, receiverFactory, senderFactory, valueFactory, parametersFactory);
        }
    }
}

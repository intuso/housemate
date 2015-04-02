package com.intuso.housemate.object.proxy;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;
import java.util.Map;

/**
 * @param <ENABLED_VALUE> the type of the value for the enabled status of the command
 * @param <PARAMETER> the type of the parameters
 * @param <PARAMETERS> the type of the parameters list
 * @param <COMMAND> the type of the command
 */
public abstract class ProxyCommand<
            ENABLED_VALUE extends ProxyValue<?, ENABLED_VALUE>,
            PARAMETER extends ProxyParameter<?, PARAMETER>,
            PARAMETERS extends ProxyList<ParameterData, PARAMETER, PARAMETERS>,
            COMMAND extends ProxyCommand<ENABLED_VALUE, PARAMETER, PARAMETERS, COMMAND>>
        extends ProxyObject<CommandData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, COMMAND, CommandListener<? super COMMAND>>
        implements Command<ENABLED_VALUE, PARAMETERS, COMMAND> {

    private int nextId;
    private Map<String, CommandPerformListener<? super COMMAND>> listenerMap = Maps.newHashMap();

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    protected ProxyCommand(Log log, ListenersFactory listenersFactory, CommandData data) {
        super(log, listenersFactory, data);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        final List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(PERFORMING_TYPE, new Receiver<PerformingPayload>() {
            @Override
            public void messageReceived(Message<PerformingPayload> message) throws HousemateException {
                CommandPerformListener<? super COMMAND> performer = listenerMap.get(message.getPayload().getOpId());
                if(message.getPayload().isPerforming()) {
                    if(performer != null)
                        performer.commandStarted(getThis());
                    for(CommandListener<? super COMMAND> listener : getObjectListeners())
                        listener.commandStarted(getThis(), "");
                } else {
                    listenerMap.remove(message.getPayload().getOpId());
                    if(performer != null)
                        performer.commandFinished(getThis());
                    for(CommandListener<? super COMMAND> listener : getObjectListeners())
                        listener.commandFinished(getThis());
                }
            }
        }));
        result.add(addMessageListener(FAILED_TYPE, new Receiver<FailedPayload>() {
            @Override
            public void messageReceived(Message<FailedPayload> message) throws HousemateException {
                CommandPerformListener<? super COMMAND> performer = listenerMap.remove(message.getPayload().getOpId());
                if(performer != null)
                    performer.commandFailed(getThis(), message.getPayload().getCause());
                for(CommandListener<? super COMMAND> listener : getObjectListeners())
                    listener.commandFailed(getThis(), message.getPayload().getCause());
            }
        }));
        addChildLoadedListener(ENABLED_ID, new ChildLoadedListener<COMMAND, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(COMMAND object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getEnabledValue().addObjectListener(new ValueListener<ENABLED_VALUE>() {
                    @Override
                    public void valueChanging(ENABLED_VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(ENABLED_VALUE value) {
                        // call our own listeners
                        boolean enabled = isEnabled();
                        for(CommandListener<? super COMMAND> listener : getObjectListeners())
                            listener.commandEnabled(getThis(), enabled);
                    }
                }));
            }
        });
        return result;
    }

    @Override
    public boolean isEnabled() {
        ENABLED_VALUE enabled = getEnabledValue();
        return enabled != null
                && enabled.getTypeInstances() != null
                && enabled.getTypeInstances().getFirstValue() != null
                && Boolean.parseBoolean(enabled.getTypeInstances().getFirstValue());
    }

    @Override
    public ENABLED_VALUE getEnabledValue() {
        return (ENABLED_VALUE) getChild(ENABLED_ID);
    }

    @Override
    public PARAMETERS getParameters() {
        return (PARAMETERS) getChild(PARAMETERS_ID);
    }

    /**
     * Performs the command without any type values. It is not correct to use this method on a command that has parameters
     * @param listener the listener for progress of the command
     */
    public final synchronized void perform(CommandPerformListener<? super COMMAND> listener) {
        perform(new TypeInstanceMap(), listener);
    }

    @Override
    public final synchronized void perform(TypeInstanceMap values, CommandPerformListener<? super COMMAND> listener) {
        String id = "" + nextId++;
        listenerMap.put(id, listener);
        sendMessage(PERFORM_TYPE, new PerformPayload(id, values));
    }
}

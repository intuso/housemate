package com.intuso.housemate.server.object.proxy;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.ParameterData;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.object.ObjectFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

public class ServerProxyCommand
        extends ServerProxyObject<CommandData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyCommand, Command.Listener<? super ServerProxyCommand>>
        implements Command<TypeInstanceMap, ServerProxyValue, ServerProxyList<ParameterData, ServerProxyParameter>, ServerProxyCommand> {

    private final static String ENABLED_DESCRIPTION = "Whether the command is enabled or not";

    private ServerProxyValue enabledValue;
    private ServerProxyList<ParameterData, ServerProxyParameter> parameters;
    private int nextId;
    private Map<String, Command.PerformListener<? super ServerProxyCommand>> listenerMap = Maps.newHashMap();

    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    protected ServerProxyCommand(Logger logger, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted CommandData data) {
        super(logger, listenersFactory, objectFactory, data);
    }

    @Override
    protected void getChildObjects() {
        enabledValue = (ServerProxyValue) getChild(CommandData.ENABLED_ID);
        parameters = (ServerProxyList<ParameterData, ServerProxyParameter>) getChild(CommandData.PARAMETERS_ID);
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // nothing to do
    }

    @Override
    public ServerProxyValue getEnabledValue() {
        return enabledValue;
    }

    @Override
    public ServerProxyList<ParameterData, ServerProxyParameter> getParameters() {
        return parameters;
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(CommandData.PERFORMING_TYPE, new Message.Receiver<ClientPayload<CommandData.PerformingPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<CommandData.PerformingPayload>> message) {
                Command.PerformListener<? super ServerProxyCommand> performer = listenerMap.get(message.getPayload().getOriginal().getOpId());
                if(message.getPayload().getOriginal().isPerforming()) {
                    if(performer != null)
                        performer.commandStarted(ServerProxyCommand.this);
                    for(Command.Listener<? super ServerProxyCommand> listener : getObjectListeners())
                        listener.commandStarted(ServerProxyCommand.this, "");
                } else {
                    listenerMap.remove(message.getPayload().getOriginal().getOpId());
                    if(performer != null)
                        performer.commandFinished(getThis());
                    for(Command.Listener<? super ServerProxyCommand> listener : getObjectListeners())
                        listener.commandFinished(getThis());
                }
            }
        }));
        result.add(addMessageListener(CommandData.FAILED_TYPE, new Message.Receiver<ClientPayload<CommandData.FailedPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<CommandData.FailedPayload>> message) {
                Command.PerformListener<? super ServerProxyCommand> performer = listenerMap.remove(message.getPayload().getOriginal().getOpId());
                if(performer != null)
                    performer.commandFailed(ServerProxyCommand.this, message.getPayload().getOriginal().getCause());
                for(Command.Listener<? super ServerProxyCommand> listener : getObjectListeners())
                    listener.commandFailed(ServerProxyCommand.this, message.getPayload().getOriginal().getCause());
            }
        }));
        return result;
    }

    @Override
    public final synchronized void perform(TypeInstanceMap values, Command.PerformListener<? super ServerProxyCommand> listener) {
        try {
            String id = "" + nextId++;
            sendMessage(CommandData.PERFORM_TYPE, new CommandData.PerformPayload(id, values));
            listenerMap.put(id, listener);
        } catch(Throwable t) {
            listener.commandFailed(getThis(), "Failed to send message to client: " + t.getMessage());
        }
    }
}

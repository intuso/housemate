package com.intuso.housemate.object.server.proxy;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
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
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.server.ClientPayload;
import com.intuso.housemate.object.server.real.ServerRealValue;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;
import java.util.Map;

public class ServerProxyCommand
        extends ServerProxyObject<CommandData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyCommand, CommandListener<? super ServerProxyCommand>>
        implements Command<ServerRealValue<Boolean>, ServerProxyList<ParameterData, ServerProxyParameter>, ServerProxyCommand> {

    private final static String ENABLED_DESCRIPTION = "Whether the command is enabled or not";

    private ServerRealValue<Boolean> enabledValue;
    private ServerProxyList<ParameterData, ServerProxyParameter> parameters;
    private int nextId;
    private Map<String, CommandPerformListener<? super ServerProxyCommand>> listenerMap = Maps.newHashMap();

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    protected ServerProxyCommand(Log log, ListenersFactory listenersFactory, Injector injector, @Assisted CommandData data) {
        super(log, listenersFactory, injector, data);
        enabledValue = new ServerRealValue<Boolean>(log, listenersFactory, ENABLED_ID, ENABLED_ID, ENABLED_DESCRIPTION, injector.getInstance(BooleanType.class), true);
    }

    @Override
    protected void getChildObjects() {
        parameters = (ServerProxyList<ParameterData, ServerProxyParameter>) getChild(PARAMETERS_ID);
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // nothing to do
    }

    @Override
    public boolean isEnabled() {
        return enabledValue.getTypedValue();
    }

    @Override
    public ServerRealValue<Boolean> getEnabledValue() {
        return enabledValue;
    }

    @Override
    public ServerProxyList<ParameterData, ServerProxyParameter> getParameters() {
        return parameters;
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(PERFORMING_TYPE, new Receiver<ClientPayload<PerformingMessageValue>>() {
            @Override
            public void messageReceived(Message<ClientPayload<PerformingMessageValue>> message) throws HousemateException {
                CommandPerformListener<? super ServerProxyCommand> performer = listenerMap.get(message.getPayload().getOriginal().getOpId());
                if(message.getPayload().getOriginal().isPerforming()) {
                    if(performer != null)
                        performer.commandStarted(ServerProxyCommand.this);
                    for(CommandListener<? super ServerProxyCommand> listener : getObjectListeners())
                        listener.commandStarted(ServerProxyCommand.this, "");
                } else {
                    listenerMap.remove(message.getPayload().getOriginal().getOpId());
                    if(performer != null)
                        performer.commandFinished(getThis());
                    for(CommandListener<? super ServerProxyCommand> listener : getObjectListeners())
                        listener.commandFinished(getThis());
                }
            }
        }));
        result.add(addMessageListener(FAILED_TYPE, new Receiver<ClientPayload<FailedMessageValue>>() {
            @Override
            public void messageReceived(Message<ClientPayload<FailedMessageValue>> message) throws HousemateException {
                CommandPerformListener<? super ServerProxyCommand> performer = listenerMap.remove(message.getPayload().getOriginal().getOpId());
                if(performer != null)
                    performer.commandFailed(ServerProxyCommand.this, message.getPayload().getOriginal().getCause());
                for(CommandListener<? super ServerProxyCommand> listener : getObjectListeners())
                    listener.commandFailed(ServerProxyCommand.this, message.getPayload().getOriginal().getCause());
            }
        }));
        return result;
    }

    @Override
    public final synchronized void perform(TypeInstanceMap values, CommandPerformListener<? super ServerProxyCommand> listener) {
        if(isEnabled()) {
            try {
                String id = "" + nextId++;
                sendMessage(PERFORM_TYPE, new PerformMessageValue(id, values));
                listenerMap.put(id, listener);
            } catch(HousemateException e) {
                listener.commandFailed(getThis(), "Failed to send message to client: " + e.getMessage());
            }
        } else
            listener.commandFailed(getThis(), "Command is not enabled");
    }
}

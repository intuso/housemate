package com.intuso.housemate.object.server.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.server.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerProxyCommand
        extends ServerProxyObject<CommandData, ListData<ParameterData>, ServerProxyList<ParameterData, ServerProxyParameter>, ServerProxyCommand, CommandListener<? super ServerProxyCommand>>
        implements Command<ServerProxyList<ParameterData, ServerProxyParameter>, ServerProxyCommand> {

    private ServerProxyList<ParameterData, ServerProxyParameter> parameters;
    private int nextId;
    private Map<String, CommandListener<? super ServerProxyCommand>> listenerMap = new HashMap<String, CommandListener<? super ServerProxyCommand>>();

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    protected ServerProxyCommand(Log log, Injector injector, @Assisted CommandData data) {
        super(log, injector, data);
    }

    @Override
    public ServerProxyList<ParameterData, ServerProxyParameter> getParameters() {
        return parameters;
    }

    @Override
    protected void getChildObjects() {
        parameters = getChild(PARAMETERS_ID);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(PERFORMING_TYPE, new Receiver<ClientPayload<PerformingMessageValue>>() {
            @Override
            public void messageReceived(Message<ClientPayload<PerformingMessageValue>> message) throws HousemateException {
                CommandListener<? super ServerProxyCommand> performer = listenerMap.get(message.getPayload().getOriginal().getOpId());
                if(message.getPayload().getOriginal().isPerforming()) {
                    if(performer != null)
                        performer.commandStarted(ServerProxyCommand.this);
                    for(CommandListener<? super ServerProxyCommand> listener : getObjectListeners())
                        listener.commandStarted(ServerProxyCommand.this);
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
                CommandListener<? super ServerProxyCommand> performer = listenerMap.remove(message.getPayload().getOriginal().getOpId());
                if(performer != null)
                    performer.commandFailed(ServerProxyCommand.this, message.getPayload().getOriginal().getCause());
                for(CommandListener<? super ServerProxyCommand> listener : getObjectListeners())
                    listener.commandFailed(ServerProxyCommand.this, message.getPayload().getOriginal().getCause());
            }
        }));
        return result;
    }

    @Override
    public final synchronized void perform(TypeInstanceMap values, CommandListener<? super ServerProxyCommand> listener) {
        String id = "" + nextId++;
        listenerMap.put(id, listener);
        try {
            sendMessage(PERFORM_TYPE, new PerformMessageValue(id, values));
        } catch(HousemateException e) {
            listener.commandFailed(getThis(), "Failed to send message to client: " + e.getMessage());
        }
    }
}

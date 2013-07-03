package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.broker.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrokerProxyCommand
        extends BrokerProxyObject<CommandWrappable, ListWrappable<ParameterWrappable>, BrokerProxyList<ParameterWrappable, BrokerProxyParameter>, BrokerProxyCommand, CommandListener<? super BrokerProxyCommand>>
        implements Command<BrokerProxyList<ParameterWrappable, BrokerProxyParameter>, BrokerProxyCommand> {

    private BrokerProxyList<ParameterWrappable, BrokerProxyParameter> parameters;
    private int nextId;
    private Map<String, CommandListener<? super BrokerProxyCommand>> listenerMap = new HashMap<String, CommandListener<? super BrokerProxyCommand>>();

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    protected BrokerProxyCommand(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, ListWrappable<ParameterWrappable>, ? extends BrokerProxyList<ParameterWrappable, BrokerProxyParameter>>> resources, CommandWrappable data) {
        super(resources, data);
    }

    @Override
    public BrokerProxyList<ParameterWrappable, BrokerProxyParameter> getParameters() {
        return parameters;
    }

    @Override
    protected void getChildObjects() {
        parameters = getWrapper(PARAMETERS_ID);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(PERFORMING_TYPE, new Receiver<ClientPayload<PerformingMessageValue>>() {
            @Override
            public void messageReceived(Message<ClientPayload<PerformingMessageValue>> message) throws HousemateException {
                CommandListener<? super BrokerProxyCommand> performer = listenerMap.get(message.getPayload().getOriginal().getOpId());
                if(message.getPayload().getOriginal().isPerforming()) {
                    if(performer != null)
                        performer.commandStarted(BrokerProxyCommand.this);
                    for(CommandListener<? super BrokerProxyCommand> listener : getObjectListeners())
                        listener.commandStarted(BrokerProxyCommand.this);
                } else {
                    listenerMap.remove(message.getPayload().getOriginal().getOpId());
                    if(performer != null)
                        performer.commandFinished(getThis());
                    for(CommandListener<? super BrokerProxyCommand> listener : getObjectListeners())
                        listener.commandFinished(getThis());
                }
            }
        }));
        result.add(addMessageListener(FAILED_TYPE, new Receiver<ClientPayload<FailedMessageValue>>() {
            @Override
            public void messageReceived(Message<ClientPayload<FailedMessageValue>> message) throws HousemateException {
                CommandListener<? super BrokerProxyCommand> performer = listenerMap.remove(message.getPayload().getOriginal().getOpId());
                if(performer != null)
                    performer.commandFailed(BrokerProxyCommand.this, message.getPayload().getOriginal().getCause());
                for(CommandListener<? super BrokerProxyCommand> listener : getObjectListeners())
                    listener.commandFailed(BrokerProxyCommand.this, message.getPayload().getOriginal().getCause());
            }
        }));
        return result;
    }

    @Override
    public final synchronized void perform(TypeInstanceMap values, CommandListener<? super BrokerProxyCommand> listener) {
        String id = "" + nextId++;
        listenerMap.put(id, listener);
        try {
            sendMessage(PERFORM_TYPE, new PerformMessageValue(id, values));
        } catch(HousemateException e) {
            listener.commandFailed(getThis(), "Failed to send message to client: " + e.getMessage());
        }
    }
}

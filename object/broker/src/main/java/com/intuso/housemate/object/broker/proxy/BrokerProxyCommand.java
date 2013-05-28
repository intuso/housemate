package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.command.argument.ArgumentWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.object.broker.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/07/12
 * Time: 18:29
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyCommand
        extends BrokerProxyObject<CommandWrappable, ListWrappable<ArgumentWrappable>, BrokerProxyList<ArgumentWrappable, BrokerProxyArgument>, BrokerProxyCommand, CommandListener<? super BrokerProxyCommand>>
        implements Command<BrokerProxyList<ArgumentWrappable, BrokerProxyArgument>, BrokerProxyCommand> {

    private BrokerProxyList<ArgumentWrappable, BrokerProxyArgument> arguments;
    private int nextId;
    private Map<String, CommandListener<? super BrokerProxyCommand>> listenerMap = new HashMap<String, CommandListener<? super BrokerProxyCommand>>();

    protected BrokerProxyCommand(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, ListWrappable<ArgumentWrappable>, ? extends BrokerProxyList<ArgumentWrappable, BrokerProxyArgument>>> resources, CommandWrappable wrappable) {
        super(resources, wrappable);
    }

    @Override
    public BrokerProxyList<ArgumentWrappable, BrokerProxyArgument> getArguments() {
        return arguments;
    }

    @Override
    protected void getChildObjects() {
        arguments = getWrapper(ARGUMENTS_FIELD);
    }

    @Override
    protected List<ListenerRegistration<?>> registerListeners() {
        List<ListenerRegistration<?>> result = super.registerListeners();
        result.add(addMessageListener(PERFORMING, new Receiver<ClientPayload<PerformingMessageValue>>() {
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
        result.add(addMessageListener(FAILED, new Receiver<ClientPayload<FailedMessageValue>>() {
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
    public final synchronized void perform(Map<String, String> values, CommandListener<? super BrokerProxyCommand> listener) {
        String id = "" + nextId++;
        listenerMap.put(id, listener);
        try {
            sendMessage(PERFORM, new PerformMessageValue(id, values));
        } catch(HousemateException e) {
            listener.commandFailed(getThis(), "Failed to send message to client: " + e.getMessage());
        }
    }
}

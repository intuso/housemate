package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.TypeInstances;
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
public abstract class ProxyCommand<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, ListWrappable<ParameterWrappable>, PL>>,
            SR extends ProxyResources<? extends HousemateObjectFactory<? extends ProxyResources<?>, ParameterWrappable, ? extends P>>,
            P extends ProxyParameter<?, ?, P>,
            PL extends ProxyList<?, ?, ParameterWrappable, P, PL>,
            C extends ProxyCommand<R, SR, P, PL, C>>
        extends ProxyObject<R, SR, CommandWrappable, ListWrappable<ParameterWrappable>, PL, C, CommandListener<? super C>>
        implements Command<PL, C> {

    private int nextId;
    private Map<String, CommandListener<? super C>> listenerMap = new HashMap<String, CommandListener<? super C>>();
    private PL parameters;

    protected ProxyCommand(R resources, SR subResources, CommandWrappable wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        parameters = getWrapper(PARAMETERS_FIELD);
        if(parameters == null)
            throw new HousemateRuntimeException("Could not unwrap command " + getId() + ", " + PARAMETERS_FIELD + " value wrapper is missing");
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(PERFORMING, new Receiver<PerformingMessageValue>() {
            @Override
            public void messageReceived(Message<PerformingMessageValue> message) throws HousemateException {
                CommandListener<? super C> performer = listenerMap.get(message.getPayload().getOpId());
                if(message.getPayload().isPerforming()) {
                    if(performer != null)
                        performer.commandStarted(getThis());
                    for(CommandListener<? super C> listener : getObjectListeners())
                        listener.commandStarted(getThis());
                } else {
                    listenerMap.remove(message.getPayload().getOpId());
                    if(performer != null)
                        performer.commandFinished(getThis());
                    for(CommandListener<? super C> listener : getObjectListeners())
                        listener.commandFinished(getThis());
                }
            }
        }));
        result.add(addMessageListener(FAILED, new Receiver<FailedMessageValue>() {
            @Override
            public void messageReceived(Message<FailedMessageValue> message) throws HousemateException {
                CommandListener<? super C> performer = listenerMap.remove(message.getPayload().getOpId());
                if(performer != null)
                    performer.commandFailed(getThis(), message.getPayload().getCause());
                for(CommandListener<? super C> listener : getObjectListeners())
                    listener.commandFailed(getThis(), message.getPayload().getCause());
            }
        }));
        return result;
    }

    @Override
    public PL getParameters() {
        return parameters;
    }

    public final synchronized void perform(CommandListener<? super C> listener) {
        perform(new TypeInstances(), listener);
    }

    @Override
    public final synchronized void perform(TypeInstances values, CommandListener<? super C> listener) {
        String id = "" + nextId++;
        listenerMap.put(id, listener);
        sendMessage(PERFORM, new PerformMessageValue(id, values));
    }
}

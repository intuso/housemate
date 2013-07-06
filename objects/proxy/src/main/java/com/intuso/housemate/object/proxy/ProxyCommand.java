package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <PARAMETER> the type of the parameters
 * @param <PARAMETERS> the type of the parameters list
 * @param <COMMAND> the type of the command
 */
public abstract class ProxyCommand<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, ListData<ParameterData>, PARAMETERS>>,
            CHILD_RESOURCES extends ProxyResources<? extends HousemateObjectFactory<? extends ProxyResources<?>, ParameterData, ? extends PARAMETER>>,
            PARAMETER extends ProxyParameter<?, ?, PARAMETER>,
            PARAMETERS extends ProxyList<?, ?, ParameterData, PARAMETER, PARAMETERS>,
            COMMAND extends ProxyCommand<RESOURCES, CHILD_RESOURCES, PARAMETER, PARAMETERS, COMMAND>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, CommandData, ListData<ParameterData>, PARAMETERS, COMMAND, CommandListener<? super COMMAND>>
        implements Command<PARAMETERS, COMMAND> {

    private int nextId;
    private Map<String, CommandListener<? super COMMAND>> listenerMap = new HashMap<String, CommandListener<? super COMMAND>>();
    private PARAMETERS parameters;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    protected ProxyCommand(RESOURCES resources, CHILD_RESOURCES childResources, CommandData data) {
        super(resources, childResources, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        parameters = getWrapper(PARAMETERS_ID);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(PERFORMING_TYPE, new Receiver<PerformingMessageValue>() {
            @Override
            public void messageReceived(Message<PerformingMessageValue> message) throws HousemateException {
                CommandListener<? super COMMAND> performer = listenerMap.get(message.getPayload().getOpId());
                if(message.getPayload().isPerforming()) {
                    if(performer != null)
                        performer.commandStarted(getThis());
                    for(CommandListener<? super COMMAND> listener : getObjectListeners())
                        listener.commandStarted(getThis());
                } else {
                    listenerMap.remove(message.getPayload().getOpId());
                    if(performer != null)
                        performer.commandFinished(getThis());
                    for(CommandListener<? super COMMAND> listener : getObjectListeners())
                        listener.commandFinished(getThis());
                }
            }
        }));
        result.add(addMessageListener(FAILED_TYPE, new Receiver<FailedMessageValue>() {
            @Override
            public void messageReceived(Message<FailedMessageValue> message) throws HousemateException {
                CommandListener<? super COMMAND> performer = listenerMap.remove(message.getPayload().getOpId());
                if(performer != null)
                    performer.commandFailed(getThis(), message.getPayload().getCause());
                for(CommandListener<? super COMMAND> listener : getObjectListeners())
                    listener.commandFailed(getThis(), message.getPayload().getCause());
            }
        }));
        return result;
    }

    @Override
    public PARAMETERS getParameters() {
        return parameters;
    }

    /**
     * Performs the command without any type values. It is not correct to use this method on a command that has parameters
     * @param listener the listener for progress of the command
     */
    public final synchronized void perform(CommandListener<? super COMMAND> listener) {
        perform(new TypeInstanceMap(), listener);
    }

    @Override
    public final synchronized void perform(TypeInstanceMap values, CommandListener<? super COMMAND> listener) {
        String id = "" + nextId++;
        listenerMap.put(id, listener);
        sendMessage(PERFORM_TYPE, new PerformMessageValue(id, values));
    }
}

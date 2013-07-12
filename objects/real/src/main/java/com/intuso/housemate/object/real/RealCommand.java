package com.intuso.housemate.object.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.Arrays;
import java.util.List;

/**
 */
public abstract class RealCommand
        extends RealObject<CommandData, ListData<ParameterData>, RealList<ParameterData, RealParameter<?>>,
            CommandListener<? super RealCommand>>
        implements Command<RealList<ParameterData, RealParameter<?>>, RealCommand> {

    private RealList<ParameterData, RealParameter<?>> realParameters;


    /**
     * @param resources {@inheritDoc}
     * @param id the command's id
     * @param name the command's name
     * @param description the command's description
     * @param parameters the command's parameters
     */
    protected RealCommand(RealResources resources, String id, String name, String description, RealParameter<?> ... parameters) {
        this(resources, id, name, description, Arrays.asList(parameters));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the command's id
     * @param name the command's name
     * @param description the command's description
     * @param parameters the command's parameters
     */
    protected RealCommand(RealResources resources, String id, String name, String description, List<RealParameter<?>> parameters) {
        super(resources, new CommandData(id, name, description));
        realParameters = new RealList<ParameterData, RealParameter<?>>(resources, PARAMETERS_ID, PARAMETERS_ID, "The parameters required by the command", parameters);
        addChild(realParameters);
    }

    @Override
    protected final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(PERFORM_TYPE, new Receiver<PerformMessageValue>() {
            @Override
            public void messageReceived(final Message<Command.PerformMessageValue> message) throws HousemateException {
                perform(message.getPayload().getValues(), new CommandListener<RealCommand>() {
                    @Override
                    public void commandStarted(RealCommand command) {
                        sendMessage(PERFORMING_TYPE, new Command.PerformingMessageValue(message.getPayload().getOpId(), true));
                    }

                    @Override
                    public void commandFinished(RealCommand command) {
                        sendMessage(PERFORMING_TYPE, new PerformingMessageValue(message.getPayload().getOpId(), false));
                    }

                    @Override
                    public void commandFailed(RealCommand command, String error) {
                        sendMessage(FAILED_TYPE, new FailedMessageValue(message.getPayload().getOpId(), error));
                    }
                });
            }
        }));
        return result;
    }

    @Override
    public RealList<ParameterData, RealParameter<?>> getParameters() {
        return realParameters;
    }

    @Override
    public void perform(TypeInstanceMap values, CommandListener<? super RealCommand> listener) {
        try {
            listener.commandStarted(this);
            perform(values);
            listener.commandFinished(this);
        } catch(Throwable t) {
            getLog().e("Failed to perform command");
            getLog().st(t);
            listener.commandFailed(this, t.getMessage());
        }
    }

    /**
     * Performs the command
     * @param values the values of the parameters to use
     * @throws HousemateException if the command fails
     */
    public abstract void perform(TypeInstanceMap values) throws HousemateException;
}

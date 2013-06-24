package com.intuso.housemate.object.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 */
public abstract class RealCommand
        extends RealObject<CommandWrappable, ListWrappable<ParameterWrappable>, RealList<ParameterWrappable, RealParameter<?>>,
            CommandListener<? super RealCommand>>
        implements Command<RealList<ParameterWrappable, RealParameter<?>>, RealCommand> {

    private RealList<ParameterWrappable, RealParameter<?>> realParameters;

    protected RealCommand(RealResources resources, String id, String name, String description, List<RealParameter<?>> parameters) {
        super(resources, new CommandWrappable(id, name, description));
        realParameters = new RealList<ParameterWrappable, RealParameter<?>>(resources, PARAMETERS_ID, PARAMETERS_ID, "The parameters required by the command", parameters);
        addWrapper(realParameters);
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
    public RealList<ParameterWrappable, RealParameter<?>> getParameters() {
        return realParameters;
    }

    @Override
    public void perform(TypeInstances values, CommandListener<? super RealCommand> listener) {
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

    public abstract void perform(TypeInstances values) throws HousemateException;
}

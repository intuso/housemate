package com.intuso.housemate.object.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.command.argument.ArgumentWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/07/12
 * Time: 22:22
 * To change this template use File | Settings | File Templates.
 */
public abstract class RealCommand
        extends RealObject<CommandWrappable, ListWrappable<ArgumentWrappable>, RealList<ArgumentWrappable, RealArgument<?>>,
            CommandListener<? super RealCommand>>
        implements Command<RealList<ArgumentWrappable, RealArgument<?>>, RealCommand> {

    private RealList<ArgumentWrappable, RealArgument<?>> realArguments;

    protected RealCommand(RealResources resources, String id, String name, String description, List<RealArgument<?>> arguments) {
        super(resources, new CommandWrappable(id, name, description));
        realArguments = new RealList<ArgumentWrappable, RealArgument<?>>(resources, ARGUMENTS_FIELD, ARGUMENTS_FIELD, "The arguments required by the command", arguments);
        addWrapper(realArguments);
    }

    @Override
    protected final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(PERFORM, new Receiver<PerformMessageValue>() {
            @Override
            public void messageReceived(final Message<Command.PerformMessageValue> message) throws HousemateException {
                perform(message.getPayload().getValues(), new CommandListener<RealCommand>() {
                    @Override
                    public void commandStarted(RealCommand command) {
                        sendMessage(PERFORMING, new Command.PerformingMessageValue(message.getPayload().getOpId(), true));
                    }

                    @Override
                    public void commandFinished(RealCommand command) {
                        sendMessage(PERFORMING, new PerformingMessageValue(message.getPayload().getOpId(), false));
                    }

                    @Override
                    public void commandFailed(RealCommand command, String error) {
                        sendMessage(FAILED, new FailedMessageValue(message.getPayload().getOpId(), error));
                    }
                });
            }
        }));
        return result;
    }

    @Override
    public RealList<ArgumentWrappable, RealArgument<?>> getArguments() {
        return realArguments;
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

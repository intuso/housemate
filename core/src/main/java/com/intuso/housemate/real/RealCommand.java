package com.intuso.housemate.real;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.comms.Message;
import com.intuso.housemate.core.comms.Receiver;
import com.intuso.housemate.core.object.command.Command;
import com.intuso.housemate.core.object.command.CommandListener;
import com.intuso.housemate.core.object.command.CommandWrappable;
import com.intuso.housemate.core.object.command.argument.ArgumentWrappable;
import com.intuso.housemate.core.object.list.ListWrappable;
import com.intuso.listeners.ListenerRegistration;

import java.util.List;
import java.util.Map;

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
    protected final List<ListenerRegistration<?>> registerListeners() {
        List<ListenerRegistration<?>> result = super.registerListeners();
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
    public void perform(Map<String, String> values, CommandListener<? super RealCommand> listener) {
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

    public abstract void perform(Map<String, String> values) throws HousemateException;
}

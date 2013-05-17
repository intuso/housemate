package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.broker.comms.ClientPayload;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.comms.Message;
import com.intuso.housemate.core.comms.Receiver;
import com.intuso.housemate.core.object.command.Command;
import com.intuso.housemate.core.object.command.CommandListener;
import com.intuso.housemate.core.object.command.CommandWrappable;
import com.intuso.housemate.core.object.command.argument.Argument;
import com.intuso.housemate.core.object.command.argument.ArgumentWrappable;
import com.intuso.housemate.core.object.list.List;
import com.intuso.housemate.core.object.list.ListWrappable;
import com.intuso.listeners.ListenerRegistration;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/07/12
 * Time: 23:40
 * To change this template use File | Settings | File Templates.
 */
public class CommandBridge
        extends BridgeObject<CommandWrappable, ListWrappable<ArgumentWrappable>,
            ListBridge<Argument<?>, ArgumentWrappable, ArgumentBridge>, CommandBridge,
            CommandListener<? super CommandBridge>>
        implements Command<ListBridge<Argument<?>, ArgumentWrappable, ArgumentBridge>, CommandBridge> {

    private Command<?, ?> proxyCommand;
    private ListBridge<Argument<?>, ArgumentWrappable, ArgumentBridge> arguments;

    public CommandBridge(BrokerBridgeResources resources, Command<? extends List<? extends Argument<?>>, ?> proxyCommand) {
        super(resources, new CommandWrappable(proxyCommand.getId(), proxyCommand.getName(), proxyCommand.getDescription()));
        this.proxyCommand = proxyCommand;
        arguments = new ListBridge<Argument<?>, ArgumentWrappable, ArgumentBridge>(resources,
                proxyCommand.getArguments(), new ArgumentConverter(resources));
        addWrapper(arguments);
    }

    @Override
    public ListBridge<Argument<?>, ArgumentWrappable, ArgumentBridge> getArguments() {
        return arguments;
    }

    @Override
    protected final java.util.List<ListenerRegistration<?>> registerListeners() {
        java.util.List<ListenerRegistration<?>> result = super.registerListeners();
        result.add(addMessageListener(PERFORM, new Receiver<ClientPayload<PerformMessageValue>>() {
            @Override
            public void messageReceived(final Message<ClientPayload<PerformMessageValue>> message) throws HousemateException {
                perform(message.getPayload().getOriginal().getValues(), new CommandListener<CommandBridge>() {
                    @Override
                    public void commandStarted(CommandBridge command) {
                        try {
                            sendMessage(PERFORMING, new PerformingMessageValue(message.getPayload().getOriginal().getOpId(), true), message.getPayload().getClient());
                        } catch(HousemateException e) {
                            getLog().e("Failed to send command started message to client");
                            getLog().e(e.getMessage());
                        }
                    }

                    @Override
                    public void commandFinished(CommandBridge command) {
                        try {
                            sendMessage(PERFORMING, new PerformingMessageValue(message.getPayload().getOriginal().getOpId(), false), message.getPayload().getClient());
                        } catch(HousemateException e) {
                            getLog().e("Failed to send command finished message to client");
                            getLog().e(e.getMessage());
                        }
                    }

                    @Override
                    public void commandFailed(CommandBridge command, String error) {
                        try {
                            sendMessage(FAILED, new FailedMessageValue(message.getPayload().getOriginal().getOpId(), error), message.getPayload().getClient());
                        } catch(HousemateException e) {
                            getLog().e("Failed to send command failed message to client");
                            getLog().e(e.getMessage());
                        }
                    }
                });
            }
        }));
        return result;
    }

    @Override
    public void perform(Map<String, String> values, final CommandListener<? super CommandBridge> listener) {
        proxyCommand.perform(values, new CommandListener<Command<?, ?>>() {
            @Override
            public void commandStarted(Command<?, ?> command) {
                listener.commandStarted(getThis());
            }

            @Override
            public void commandFinished(Command<?, ?> command) {
                listener.commandFinished(getThis());
            }

            @Override
            public void commandFailed(Command<?, ?> command, String error) {
                listener.commandFailed(getThis(), error);
            }
        });
    }

    private final static class ArgumentConverter implements Function<Argument<?>, ArgumentBridge> {

        private BrokerBridgeResources resources;

        private ArgumentConverter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public ArgumentBridge apply(@Nullable Argument<?> argument) {
            return new ArgumentBridge(resources, argument);
        }
    };
}

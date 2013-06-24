package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.broker.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;

import javax.annotation.Nullable;

/**
 */
public class CommandBridge
        extends BridgeObject<CommandWrappable, ListWrappable<ParameterWrappable>,
            ListBridge<ParameterWrappable, Parameter<?>, ParameterBridge>, CommandBridge,
        CommandListener<? super CommandBridge>>
        implements Command<ListBridge<ParameterWrappable, Parameter<?>, ParameterBridge>, CommandBridge> {

    private Command<?, ?> proxyCommand;
    private ListBridge<ParameterWrappable, Parameter<?>, ParameterBridge> parameters;

    public CommandBridge(BrokerBridgeResources resources, Command<? extends List<? extends Parameter<?>>, ?> proxyCommand) {
        super(resources, new CommandWrappable(proxyCommand.getId(), proxyCommand.getName(), proxyCommand.getDescription()));
        this.proxyCommand = proxyCommand;
        parameters = new ListBridge<ParameterWrappable, Parameter<?>, ParameterBridge>(resources,
                proxyCommand.getParameters(), new ParameterBridge.Converter(resources));
        addWrapper(parameters);
    }

    @Override
    public ListBridge<ParameterWrappable, Parameter<?>, ParameterBridge> getParameters() {
        return parameters;
    }

    @Override
    protected final java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(PERFORM_TYPE, new Receiver<ClientPayload<PerformMessageValue>>() {
            @Override
            public void messageReceived(final Message<ClientPayload<PerformMessageValue>> message) throws HousemateException {
                perform(message.getPayload().getOriginal().getValues(), new CommandListener<CommandBridge>() {
                    @Override
                    public void commandStarted(CommandBridge command) {
                        try {
                            for(CommandListener<? super CommandBridge> listener : getObjectListeners())
                                listener.commandStarted(getThis());
                            sendMessage(PERFORMING_TYPE, new PerformingMessageValue(message.getPayload().getOriginal().getOpId(), true), message.getPayload().getClient());
                        } catch(HousemateException e) {
                            getLog().e("Failed to send command started message to client");
                            getLog().e(e.getMessage());
                        }
                    }

                    @Override
                    public void commandFinished(CommandBridge command) {
                        try {
                            for(CommandListener<? super CommandBridge> listener : getObjectListeners())
                                listener.commandFinished(getThis());
                            sendMessage(PERFORMING_TYPE, new PerformingMessageValue(message.getPayload().getOriginal().getOpId(), false), message.getPayload().getClient());
                        } catch(HousemateException e) {
                            getLog().e("Failed to send command finished message to client");
                            getLog().e(e.getMessage());
                        }
                    }

                    @Override
                    public void commandFailed(CommandBridge command, String error) {
                        try {

                            for(CommandListener<? super CommandBridge> listener : getObjectListeners())
                                listener.commandFailed(getThis(), error);
                            sendMessage(FAILED_TYPE, new FailedMessageValue(message.getPayload().getOriginal().getOpId(), error), message.getPayload().getClient());
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
    public void perform(TypeInstances values, final CommandListener<? super CommandBridge> listener) {
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

    public static class Converter implements Function<Command<?, ?>, CommandBridge> {

        private final BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public CommandBridge apply(@Nullable Command<?, ?> command) {
            return new CommandBridge(resources, command);
        }
    }
}

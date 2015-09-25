package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.ParameterData;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Parameter;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class CommandBridge
        extends BridgeObject<CommandData, HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>, CommandBridge,
            Command.Listener<? super CommandBridge>>
        implements Command<TypeInstanceMap, ValueBridge, ConvertingListBridge<ParameterData, Parameter<?>, ParameterBridge>, CommandBridge> {

    private Command<TypeInstanceMap, ?, ?, ?> proxyCommand;
    private ValueBridge enabledValue;
    private ConvertingListBridge<ParameterData, Parameter<?>, ParameterBridge> parameters;

    public CommandBridge(Log log, ListenersFactory listenersFactory, Command<?, ?, ?, ?> proxyCommand) {
        super(log, listenersFactory,
                new CommandData(proxyCommand.getId(), proxyCommand.getName(), proxyCommand.getDescription()));
        this.proxyCommand = (Command<TypeInstanceMap, ?, ?, ?>) proxyCommand;
        enabledValue = new ValueBridge(log, listenersFactory, proxyCommand.getEnabledValue());
        parameters = new ConvertingListBridge<>(log, listenersFactory, proxyCommand.getParameters(), new ParameterBridge.Converter(log, listenersFactory));
        addChild(enabledValue);
        addChild(parameters);
    }

    @Override
    public ValueBridge getEnabledValue() {
        return enabledValue;
    }

    @Override
    public ConvertingListBridge<ParameterData, Parameter<?>, ParameterBridge> getParameters() {
        return parameters;
    }

    @Override
    protected final java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(CommandData.PERFORM_TYPE, new Message.Receiver<ClientPayload<CommandData.PerformPayload>>() {
            @Override
            public void messageReceived(final Message<ClientPayload<CommandData.PerformPayload>> message) {
                perform(message.getPayload().getOriginal().getValues(), new Command.PerformListener<CommandBridge>() {
                    @Override
                    public void commandStarted(CommandBridge command) {
                        try {
                            for(Command.Listener<? super CommandBridge> listener : getObjectListeners())
                                listener.commandStarted(getThis(), "");
                            sendMessage(CommandData.PERFORMING_TYPE, new CommandData.PerformingPayload(message.getPayload().getOriginal().getOpId(), true), message.getPayload().getClient());
                        } catch(Throwable t) {
                            getLog().e("Failed to send command started message to client");
                            getLog().e(t.getMessage());
                        }
                    }

                    @Override
                    public void commandFinished(CommandBridge command) {
                        try {
                            for(Command.Listener<? super CommandBridge> listener : getObjectListeners())
                                listener.commandFinished(getThis());
                            sendMessage(CommandData.PERFORMING_TYPE, new CommandData.PerformingPayload(message.getPayload().getOriginal().getOpId(), false), message.getPayload().getClient());
                        } catch(Throwable t) {
                            getLog().e("Failed to send command finished message to client");
                            getLog().e(t.getMessage());
                        }
                    }

                    @Override
                    public void commandFailed(CommandBridge command, String error) {
                        try {
                            for(Command.Listener<? super CommandBridge> listener : getObjectListeners())
                                listener.commandFailed(getThis(), error);
                            sendMessage(CommandData.FAILED_TYPE, new CommandData.FailedPayload(message.getPayload().getOriginal().getOpId(), error), message.getPayload().getClient());
                        } catch(Throwable t) {
                            getLog().e("Failed to send command failed message to client");
                            getLog().e(t.getMessage());
                        }
                    }
                });
            }
        }));
        return result;
    }

    @Override
    public void perform(TypeInstanceMap values, final Command.PerformListener<? super CommandBridge> listener) {
        proxyCommand.perform(values, new Command.PerformListener<Command<?, ?, ?, ?>>() {
            @Override
            public void commandStarted(Command<?, ?, ?, ?> command) {
                listener.commandStarted(getThis());
            }

            @Override
            public void commandFinished(Command<?, ?, ?, ?> command) {
                listener.commandFinished(getThis());
            }

            @Override
            public void commandFailed(Command<?, ?, ?, ?> command, String error) {
                listener.commandFailed(getThis(), error);
            }
        });
    }

    public static class Converter implements Function<Command<?, ?, ?, ?>, CommandBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public CommandBridge apply(Command<?, ?, ?, ?> command) {
            return new CommandBridge(log, listenersFactory, command);
        }
    }
}

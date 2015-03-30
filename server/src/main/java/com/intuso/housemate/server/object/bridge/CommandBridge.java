package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.server.client.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class CommandBridge
        extends BridgeObject<CommandData, HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>, CommandBridge,
            CommandListener<? super CommandBridge>>
        implements Command<ValueBridge, ConvertingListBridge<ParameterData, Parameter<?>, ParameterBridge>, CommandBridge> {

    private Command<?, ?, ?> proxyCommand;
    private ValueBridge enabledValue;
    private ConvertingListBridge<ParameterData, Parameter<?>, ParameterBridge> parameters;

    public CommandBridge(Log log, ListenersFactory listenersFactory, Command<?, ? extends List<? extends Parameter<?>>, ?> proxyCommand) {
        super(log, listenersFactory,
                new CommandData(proxyCommand.getId(), proxyCommand.getName(), proxyCommand.getDescription()));
        this.proxyCommand = proxyCommand;
        enabledValue = new ValueBridge(log, listenersFactory, proxyCommand.getEnabledValue());
        parameters = new ConvertingListBridge<>(log, listenersFactory, proxyCommand.getParameters(), new ParameterBridge.Converter(log, listenersFactory));
        addChild(enabledValue);
        addChild(parameters);
    }

    @Override
    public boolean isEnabled() {
        java.util.List<Boolean> enableds = RealType.deserialiseAll(BooleanType.SERIALISER, enabledValue.getTypeInstances());
        return enableds != null && enableds.size() > 0 && enableds.get(0) != null ? enableds.get(0) : false;
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
        result.add(addMessageListener(PERFORM_TYPE, new Receiver<ClientPayload<PerformPayload>>() {
            @Override
            public void messageReceived(final Message<ClientPayload<PerformPayload>> message) throws HousemateException {
                perform(message.getPayload().getOriginal().getValues(), new CommandPerformListener<CommandBridge>() {
                    @Override
                    public void commandStarted(CommandBridge command) {
                        try {
                            for(CommandListener<? super CommandBridge> listener : getObjectListeners())
                                listener.commandStarted(getThis(), "");
                            sendMessage(PERFORMING_TYPE, new PerformingPayload(message.getPayload().getOriginal().getOpId(), true), message.getPayload().getClient());
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
                            sendMessage(PERFORMING_TYPE, new PerformingPayload(message.getPayload().getOriginal().getOpId(), false), message.getPayload().getClient());
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
                            sendMessage(FAILED_TYPE, new FailedPayload(message.getPayload().getOriginal().getOpId(), error), message.getPayload().getClient());
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
    public void perform(TypeInstanceMap values, final CommandPerformListener<? super CommandBridge> listener) {
        proxyCommand.perform(values, new CommandPerformListener<Command<?, ?, ?>>() {
            @Override
            public void commandStarted(Command<?, ?, ?> command) {
                listener.commandStarted(getThis());
            }

            @Override
            public void commandFinished(Command<?, ?, ?> command) {
                listener.commandFinished(getThis());
            }

            @Override
            public void commandFailed(Command<?, ?, ?> command, String error) {
                listener.commandFailed(getThis(), error);
            }
        });
    }

    public static class Converter implements Function<Command<?, ?, ?>, CommandBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public CommandBridge apply(Command<?, ?, ?> command) {
            return new CommandBridge(log, listenersFactory, command);
        }
    }
}

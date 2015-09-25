package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.real.api.internal.impl.type.BooleanType;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.ParameterData;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 */
public abstract class RealCommand
        extends RealObject<CommandData, HousemateData<?>, RealObject<?, ?, ?, ?>, Command.Listener<? super RealCommand>>
        implements Command<TypeInstanceMap, RealValue<Boolean>, RealList<ParameterData, RealParameter<?>>, RealCommand> {

    private final static String ENABLED_DESCRIPTION = "Whether the command is enabled or not";

    private final RealValue<Boolean> enabledValue;
    private final RealList<ParameterData, RealParameter<?>> parameters;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the command's id
     * @param name the command's name
     * @param description the command's description
     * @param parameters the command's parameters
     */
    protected RealCommand(Log log, ListenersFactory listenersFactory, String id, String name, String description, RealParameter<?>... parameters) {
        this(log, listenersFactory, id, name, description, Arrays.asList(parameters));
    }

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the command's id
     * @param name the command's name
     * @param description the command's description
     * @param parameters the command's parameters
     */
    protected RealCommand(Log log, ListenersFactory listenersFactory, String id, String name, String description, List<RealParameter<?>> parameters) {
        super(log, listenersFactory, new CommandData(id, name, description));
        enabledValue = new RealValue<>(log, listenersFactory, CommandData.ENABLED_ID, CommandData.ENABLED_ID, ENABLED_DESCRIPTION, new BooleanType(log, listenersFactory), true);
        this.parameters = new RealList<>(log, listenersFactory, CommandData.PARAMETERS_ID, CommandData.PARAMETERS_ID, "The parameters required by the command", parameters);
        addChild(enabledValue);
        addChild(this.parameters);
    }

    @Override
    protected final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(CommandData.PERFORM_TYPE, new Message.Receiver<CommandData.PerformPayload>() {
            @Override
            public void messageReceived(final Message<CommandData.PerformPayload> message) {
                perform(message.getPayload().getValues(), new Command.PerformListener<RealCommand>() {

                    @Override
                    public void commandStarted(RealCommand command) {
                        sendMessage(CommandData.PERFORMING_TYPE, new CommandData.PerformingPayload(message.getPayload().getOpId(), true));
                    }

                    @Override
                    public void commandFinished(RealCommand command) {
                        sendMessage(CommandData.PERFORMING_TYPE, new CommandData.PerformingPayload(message.getPayload().getOpId(), false));
                    }

                    @Override
                    public void commandFailed(RealCommand command, String error) {
                        sendMessage(CommandData.FAILED_TYPE, new CommandData.FailedPayload(message.getPayload().getOpId(), error));
                    }
                });
            }
        }));
        return result;
    }

    @Override
    public RealValue<Boolean> getEnabledValue() {
        return enabledValue;
    }

    @Override
    public RealList<ParameterData, RealParameter<?>> getParameters() {
        return parameters;
    }

    @Override
    public void perform(TypeInstanceMap values, Command.PerformListener<? super RealCommand> listener) {
        try {
            listener.commandStarted(this);
            perform(values);
            listener.commandFinished(this);
        } catch(Throwable t) {
            getLog().e("Failed to perform command", t);
            listener.commandFailed(this, t.getMessage());
        }
    }

    /**
     * Performs the command
     * @param values the values of the parameters to use
     */
    public abstract void perform(TypeInstanceMap values);
}
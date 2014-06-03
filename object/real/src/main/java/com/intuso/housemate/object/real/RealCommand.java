package com.intuso.housemate.object.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 */
public abstract class RealCommand
        extends RealObject<CommandData, HousemateData<?>, RealObject<?, ?, ?, ?>, CommandListener<? super RealCommand>>
        implements Command<RealValue<Boolean>, RealList<ParameterData, RealParameter<?>>, RealCommand> {

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
        enabledValue = new RealValue<Boolean>(log, listenersFactory, ENABLED_ID, ENABLED_ID, ENABLED_DESCRIPTION, new BooleanType(log, listenersFactory), true);
        this.parameters = new RealList<ParameterData, RealParameter<?>>(log, listenersFactory, PARAMETERS_ID, PARAMETERS_ID, "The parameters required by the command", parameters);
        addChild(enabledValue);
        addChild(this.parameters);
    }

    @Override
    protected final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(PERFORM_TYPE, new Receiver<PerformPayload>() {
            @Override
            public void messageReceived(final Message<PerformPayload> message) throws HousemateException {
                perform(message.getPayload().getValues(), new CommandPerformListener<RealCommand>() {

                    @Override
                    public void commandStarted(RealCommand command) {
                        sendMessage(PERFORMING_TYPE, new PerformingPayload(message.getPayload().getOpId(), true));
                    }

                    @Override
                    public void commandFinished(RealCommand command) {
                        sendMessage(PERFORMING_TYPE, new PerformingPayload(message.getPayload().getOpId(), false));
                    }

                    @Override
                    public void commandFailed(RealCommand command, String error) {
                        sendMessage(FAILED_TYPE, new FailedPayload(message.getPayload().getOpId(), error));
                    }
                });
            }
        }));
        return result;
    }

    @Override
    public boolean isEnabled() {
        return enabledValue.getTypedValue();
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
    public void perform(TypeInstanceMap values, CommandPerformListener<? super RealCommand> listener) {
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
     * @throws HousemateException if the command fails
     */
    public abstract void perform(TypeInstanceMap values) throws HousemateException;
}

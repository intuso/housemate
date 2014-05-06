package com.intuso.housemate.object.server.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public abstract class ServerRealCommand
        extends ServerRealObject<CommandData, HousemateData<?>,
            ServerRealObject<?, ?, ?, ?>, CommandListener<? super ServerRealCommand>>
        implements Command<ServerRealValue<Boolean>, ServerRealList<ParameterData, ServerRealParameter<?>>, ServerRealCommand> {

    private final static String ENABLED_DESCRIPTION = "Whether the command is enabled or not";
    private final static String PARAMETERS_DESCRIPTION = "The parameters required by the command";

    private ServerRealValue<Boolean> enabledValue;
    private ServerRealList<ParameterData, ServerRealParameter<?>> parameters;

    /**
     * @param log {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param parameters the command's parameters
     */
    protected ServerRealCommand(Log log, ListenersFactory listenersFactory, String id, String name, String description, List<ServerRealParameter<?>> parameters) {
        super(log, listenersFactory, new CommandData(id, name, description));
        this.enabledValue = new ServerRealValue<Boolean>(log, listenersFactory, ENABLED_ID, ENABLED_ID, ENABLED_DESCRIPTION, new BooleanType(log, listenersFactory), true);
        this.parameters = new ServerRealList<ParameterData, ServerRealParameter<?>>(log, listenersFactory, PARAMETERS_ID, PARAMETERS_ID, PARAMETERS_DESCRIPTION, parameters);
        addChild(this.parameters);
    }

    @Override
    public boolean isEnabled() {
        return enabledValue.getTypedValue();
    }

    @Override
    public ServerRealValue<Boolean> getEnabledValue() {
        return enabledValue;
    }

    @Override
    public ServerRealList<ParameterData, ServerRealParameter<?>> getParameters() {
        return parameters;
    }

    @Override
    public void perform(TypeInstanceMap values, CommandPerformListener<? super ServerRealCommand> listener) {
        try {
            if(listener != null)
                listener.commandStarted(this);
            perform(values);
            if(listener != null)
                listener.commandFinished(this);
        } catch(Throwable t) {
            getLog().e("Failed to perform command", t);
            if(listener != null)
                listener.commandFailed(this, t.getMessage());
        }
    }

    /**
     * Perform the command
     * @param values the values of the parameters
     * @throws HousemateException
     */
    protected abstract void perform(TypeInstanceMap values) throws HousemateException;
}

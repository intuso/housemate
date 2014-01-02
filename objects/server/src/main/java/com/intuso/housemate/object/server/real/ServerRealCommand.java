package com.intuso.housemate.object.server.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;

import java.util.List;

public abstract class ServerRealCommand
        extends ServerRealObject<CommandData, ListData<ParameterData>,
        ServerRealList<ParameterData, ServerRealParameter<?>>, CommandListener<? super ServerRealCommand>>
        implements Command<ServerRealList<ParameterData, ServerRealParameter<?>>, ServerRealCommand> {

    private final static String PARAMETERS_DESCRIPTION = "The parameters required by the command";

    private ServerRealList<ParameterData, ServerRealParameter<?>> realParameters;

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param parameters the command's parameters
     */
    protected ServerRealCommand(ServerRealResources resources, String id, String name, String description, List<ServerRealParameter<?>> parameters) {
        super(resources, new CommandData(id, name, description));
        realParameters = new ServerRealList<ParameterData, ServerRealParameter<?>>(resources, PARAMETERS_ID, PARAMETERS_ID, PARAMETERS_DESCRIPTION, parameters);
        addChild(realParameters);
    }

    @Override
    public ServerRealList<ParameterData, ServerRealParameter<?>> getParameters() {
        return realParameters;
    }

    @Override
    public void perform(TypeInstanceMap values, CommandListener<? super ServerRealCommand> listener) {
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
     * Perform the command
     * @param values the values of the parameters
     * @throws HousemateException
     */
    public abstract void perform(TypeInstanceMap values) throws HousemateException;
}

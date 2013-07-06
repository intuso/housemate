package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;

import java.util.List;

public abstract class BrokerRealCommand
        extends BrokerRealObject<CommandData, ListData<ParameterData>,
            BrokerRealList<ParameterData, BrokerRealParameter<?>>, CommandListener<? super BrokerRealCommand>>
        implements Command<BrokerRealList<ParameterData, BrokerRealParameter<?>>, BrokerRealCommand> {

    private final static String PARAMETERS_DESCRIPTION = "The parameters required by the command";

    private BrokerRealList<ParameterData, BrokerRealParameter<?>> realParameters;

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param parameters the command's parameters
     */
    protected BrokerRealCommand(BrokerRealResources resources, String id, String name, String description, List<BrokerRealParameter<?>> parameters) {
        super(resources, new CommandData(id, name, description));
        realParameters = new BrokerRealList<ParameterData, BrokerRealParameter<?>>(resources, PARAMETERS_ID, PARAMETERS_ID, PARAMETERS_DESCRIPTION, parameters);
        addWrapper(realParameters);
    }

    @Override
    public BrokerRealList<ParameterData, BrokerRealParameter<?>> getParameters() {
        return realParameters;
    }

    @Override
    public void perform(TypeInstanceMap values, CommandListener<? super BrokerRealCommand> listener) {
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

    /**
     * Perform the command
     * @param values the values of the parameters
     * @throws HousemateException
     */
    public abstract void perform(TypeInstanceMap values) throws HousemateException;
}

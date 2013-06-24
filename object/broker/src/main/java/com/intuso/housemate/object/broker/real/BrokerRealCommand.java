package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.TypeInstances;

import java.util.List;

/**
 */
public abstract class BrokerRealCommand
        extends BrokerRealObject<CommandWrappable, ListWrappable<ParameterWrappable>,
            BrokerRealList<ParameterWrappable, BrokerRealParameter<?>>, CommandListener<? super BrokerRealCommand>>
        implements Command<BrokerRealList<ParameterWrappable, BrokerRealParameter<?>>, BrokerRealCommand> {

    private final static String PARAMETERS_DESCRIPTION = "The parameters required by the command";

    private BrokerRealList<ParameterWrappable, BrokerRealParameter<?>> realParameters;

    protected BrokerRealCommand(BrokerRealResources resources, String id, String name, String description, List<BrokerRealParameter<?>> parameters) {
        super(resources, new CommandWrappable(id, name, description));
        realParameters = new BrokerRealList<ParameterWrappable, BrokerRealParameter<?>>(resources, PARAMETERS_ID, PARAMETERS_ID, PARAMETERS_DESCRIPTION, parameters);
        addWrapper(realParameters);
    }

    @Override
    public BrokerRealList<ParameterWrappable, BrokerRealParameter<?>> getParameters() {
        return realParameters;
    }

    @Override
    public void perform(TypeInstances values, CommandListener<? super BrokerRealCommand> listener) {
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

    public abstract void perform(TypeInstances values) throws HousemateException;
}

package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.argument.ArgumentWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.TypeInstances;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/07/12
 * Time: 22:22
 * To change this template use File | Settings | File Templates.
 */
public abstract class BrokerRealCommand
        extends BrokerRealObject<CommandWrappable, ListWrappable<ArgumentWrappable>,
            BrokerRealList<ArgumentWrappable, BrokerRealArgument<?>>, CommandListener<? super BrokerRealCommand>>
        implements Command<BrokerRealList<ArgumentWrappable, BrokerRealArgument<?>>, BrokerRealCommand> {

    private final static String ARGUMENTS_DESCRIPTION = "The arguments required by the command";

    private BrokerRealList<ArgumentWrappable, BrokerRealArgument<?>> realArguments;

    protected BrokerRealCommand(BrokerRealResources resources, String id, String name, String description, List<BrokerRealArgument<?>> arguments) {
        super(resources, new CommandWrappable(id, name, description));
        realArguments = new BrokerRealList<ArgumentWrappable, BrokerRealArgument<?>>(resources, ARGUMENTS_FIELD, ARGUMENTS_FIELD, ARGUMENTS_DESCRIPTION, arguments);
        addWrapper(realArguments);
    }

    @Override
    public BrokerRealList<ArgumentWrappable, BrokerRealArgument<?>> getArguments() {
        return realArguments;
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

package com.intuso.housemate.broker.plugin.consequence;

import com.google.common.base.Joiner;
import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.broker.object.bridge.RootObjectBridge;
import com.intuso.housemate.object.broker.real.BrokerRealProperty;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.consequence.BrokerRealConsequence;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/08/12
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */
@FactoryInformation(id = "perform-command", name = "Perform Command", description = "Perform a command in the system")
public class PerformCommand extends BrokerRealConsequence implements ObjectLifecycleListener {

    private final BrokerRealProperty<RealObjectType.Reference<BaseObject<?>>> commandPath;
    private Command<?, ?> command;
    private ListenerRegistration commandLifecycleListenerRegistration = null;

    private CommandListener listener = new CommandListener<Command<?, ?>>() {
        @Override
        public void commandStarted(Command<?, ?> command) {
            consequenceExecuting(true);
        }

        @Override
        public void commandFinished(Command<?, ?> command) {
            consequenceExecuting(false);
        }

        @Override
        public void commandFailed(Command<?, ?> command, String error) {
            consequenceExecuting(false);
            setError("Failed to perform command: " + error);
        }
    };

    public PerformCommand(BrokerRealResources resources, String id, String name, String description, RootObjectBridge root) {
        super(resources, id, name, description);
        commandPath = new BrokerRealProperty<RealObjectType.Reference<BaseObject<?>>>(resources, "command-path", "Command Path", "The path to the command to perform",
                new RealObjectType(resources.getRealResources(), root), null);
        getProperties().add(commandPath);
        addPropertyListener(root);
    }

    private void addPropertyListener(final Root<?, ?> root) {
        commandPath.addObjectListener(new ValueListener<BrokerRealProperty<RealObjectType.Reference<BaseObject<?>>>>() {

            @Override
            public void valueChanging(BrokerRealProperty<RealObjectType.Reference<BaseObject<?>>> value) {
                if(commandLifecycleListenerRegistration != null)
                    commandLifecycleListenerRegistration.removeListener();
            }

            @Override
            public void valueChanged(BrokerRealProperty<RealObjectType.Reference<BaseObject<?>>> property) {
                String[] path = property.getTypedValue().getPath();
                commandLifecycleListenerRegistration = root.addObjectLifecycleListener(path, PerformCommand.this);
                HousemateObject<?, ?, ?, ?, ?> object = root.getWrapper(path);
                if(object == null)
                    setError("Cannot find an object at path " + commandPath.getValue());
                else {
                    objectCreated(path, object);
                }
            }
        });
        String[] path = commandPath.getTypedValue() != null ? commandPath.getTypedValue().getPath() : null;
        if(path != null)
            commandLifecycleListenerRegistration = root.addObjectLifecycleListener(path, PerformCommand.this);
    }

    @Override
    public void objectCreated(String[] path, HousemateObject<?, ?, ?, ?, ?> object) {
        if(!(object instanceof Command))
            setError("Object at path " + commandPath.getTypedValue() + " is not a command");
        else {
            setError(null);
            command = (Command<?, ?>)object;
        }
    }

    @Override
    public void objectRemoved(String[] path, HousemateObject<?, ?, ?, ?, ?> object) {
        command = null;
        setError("Cannot find an object at path " + commandPath.getTypedValue());
    }

    @Override
    public void _execute() throws HousemateException {
        if(command != null) {
            getLog().w("Executing " + Joiner.on("/").join(commandPath.getTypedValue().getPath()));
            command.perform(new TypeInstances(), listener);
        } else
            getLog().w("Cannot execute consequence, no command at " + Joiner.on("/").join(commandPath.getTypedValue().getPath()));
    }
}

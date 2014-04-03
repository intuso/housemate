package com.intuso.housemate.server.plugin.main.task;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.server.real.ServerRealProperty;
import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.server.object.bridge.RootObjectBridge;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 */
@FactoryInformation(id = "perform-command", name = "Perform Command", description = "Perform a command in the system")
public class PerformCommand extends ServerRealTask implements ObjectLifecycleListener {

    private final ServerRealProperty<RealObjectType.Reference<BaseHousemateObject<?>>> commandPath;
        private Command<?, ?> command;
        private ListenerRegistration commandLifecycleListenerRegistration = null;

        private CommandListener listener = new CommandListener<Command<?, ?>>() {
            @Override
            public void commandStarted(Command<?, ?> command) {
                // do nothing
            }

        @Override
        public void commandFinished(Command<?, ?> command) {
            // do nothing
        }

        @Override
        public void commandFailed(Command<?, ?> command, String error) {
            setError("Failed to perform command: " + error);
        }
    };

    @Inject
    public PerformCommand(Log log, String id, String name, String description,
                          ServerRealTaskOwner owner, RootObjectBridge root,
                          RealObjectType<BaseHousemateObject<?>> realObjectType) {
        super(log, id, name, description, owner);
        commandPath = new ServerRealProperty<RealObjectType.Reference<BaseHousemateObject<?>>>(log, "command-path", "Command Path", "The path to the command to perform",
                realObjectType, (List)null);
        getProperties().add(commandPath);
        addPropertyListener(root);
    }

    private void addPropertyListener(final Root<?> root) {
        commandPath.addObjectListener(new ValueListener<ServerRealProperty<RealObjectType.Reference<BaseHousemateObject<?>>>>() {

            @Override
            public void valueChanging(ServerRealProperty<RealObjectType.Reference<BaseHousemateObject<?>>> value) {
                if(commandLifecycleListenerRegistration != null)
                    commandLifecycleListenerRegistration.removeListener();
            }

            @Override
            public void valueChanged(ServerRealProperty<RealObjectType.Reference<BaseHousemateObject<?>>> property) {
                String[] path = property.getTypedValue().getPath();
                commandLifecycleListenerRegistration = root.addObjectLifecycleListener(path, PerformCommand.this);
                HousemateObject<?, ?, ?, ?> object = root.getObject(path);
                if(object == null)
                    setError("Cannot find an object at path " + Arrays.toString(path));
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
    public void objectCreated(String[] path, HousemateObject<?, ?, ?, ?> object) {
        if(!(object instanceof Command))
            setError("Object at path " + commandPath.getTypedValue() + " is not a command");
        else {
            setError(null);
            command = (Command<?, ?>)object;
        }
    }

    @Override
    public void objectRemoved(String[] path, HousemateObject<?, ?, ?, ?> object) {
        command = null;
        setError("Cannot find an object at path " + commandPath.getTypedValue());
    }

    @Override
    public void execute() throws HousemateException {
        if(command != null) {
            getLog().w("Executing " + Joiner.on("/").join(commandPath.getTypedValue().getPath()));
            command.perform(new TypeInstanceMap(), listener);
        } else
            getLog().w("Cannot execute task, no command at " + Joiner.on("/").join(commandPath.getTypedValue().getPath()));
    }
}
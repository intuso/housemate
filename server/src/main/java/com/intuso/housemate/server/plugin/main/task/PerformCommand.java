package com.intuso.housemate.server.plugin.main.task;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealTask;
import com.intuso.housemate.object.real.factory.task.RealTaskOwner;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
@TypeInfo(id = "perform-command", name = "Perform Command", description = "Perform a command in the system")
public class PerformCommand extends RealTask implements ObjectLifecycleListener {

    private final RealProperty<RealObjectType.Reference<BaseHousemateObject<?>>> commandPath;
    private Command<?, ?, ?> command;
    private ListenerRegistration commandLifecycleListenerRegistration = null;

    private CommandPerformListener listener = new CommandPerformListener<Command<?, ?, ?>>() {
        @Override
        public void commandStarted(Command<?, ?, ?> command) {
            // do nothing
        }

        @Override
        public void commandFinished(Command<?, ?, ?> command) {
            // do nothing
        }

        @Override
        public void commandFailed(Command<?, ?, ?> command, String error) {
            setError("Failed to perform command: " + error);
        }
    };

    @Inject
    public PerformCommand(Log log,
                          ListenersFactory listenersFactory,
                          @Assisted TaskData data,
                          @Assisted RealTaskOwner owner,
                          RootBridge root, RealObjectType<BaseHousemateObject<?>> realObjectType) {
        super(log, listenersFactory, data, owner);
        commandPath = new RealProperty<RealObjectType.Reference<BaseHousemateObject<?>>>(log, listenersFactory,
                "command-path", "Command Path", "The path to the command to perform", realObjectType, (List)null);
        getProperties().add(commandPath);
        addPropertyListener(root);
    }

    private void addPropertyListener(final RootBridge root) {
        commandPath.addObjectListener(new ValueListener<RealProperty<RealObjectType.Reference<BaseHousemateObject<?>>>>() {

            @Override
            public void valueChanging(RealProperty<RealObjectType.Reference<BaseHousemateObject<?>>> value) {
                if(commandLifecycleListenerRegistration != null)
                    commandLifecycleListenerRegistration.removeListener();
            }

            @Override
            public void valueChanged(RealProperty<RealObjectType.Reference<BaseHousemateObject<?>>> property) {
                String[] path = property.getTypedValue().getPath();
                commandLifecycleListenerRegistration = root.addObjectLifecycleListener(path, PerformCommand.this);
                HousemateObject<?, ?, ?, ?> object = HousemateObject.getChild((HousemateObject<?,?,?,?>) root, path, 1);
                if(object == null)
                    setError("Cannot find an object at path " + Joiner.on("/").join(path));
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
            setError("Object at path " + Joiner.on("/").join(commandPath.getTypedValue().getPath()) + " is not a command");
        else {
            setError(null);
            command = (Command<?, ?, ?>)object;
        }
    }

    @Override
    public void objectRemoved(String[] path, HousemateObject<?, ?, ?, ?> object) {
        command = null;
        setError("Cannot find an object at path " + Joiner.on("/").join(commandPath.getTypedValue().getPath()));
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

package com.intuso.housemate.server.plugin.main.task;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.api.internal.RealTask;
import com.intuso.housemate.client.real.api.internal.factory.task.RealTaskOwner;
import com.intuso.housemate.client.real.api.internal.impl.type.RealObjectType;
import com.intuso.housemate.comms.api.internal.payload.TaskData;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.housemate.plugin.api.internal.TypeInfo;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
@TypeInfo(id = "perform-command", name = "Perform Command", description = "Perform a command in the system")
public class PerformCommand extends RealTask implements ObjectLifecycleListener {

    private final RealProperty<RealObjectType.Reference<BaseHousemateObject<?>>> commandPath;
    private Command<TypeInstanceMap, ?, ?, ?> command;
    private ListenerRegistration commandLifecycleListenerRegistration = null;

    private Command.PerformListener<Command<?, ?, ?, ?>> listener = new Command.PerformListener<Command<?, ?, ?, ?>>() {
        @Override
        public void commandStarted(Command<?, ?, ?, ?> command) {
            // do nothing
        }

        @Override
        public void commandFinished(Command<?, ?, ?, ?> command) {
            // do nothing
        }

        @Override
        public void commandFailed(Command<?, ?, ?, ?> command, String error) {
            setError("Failed to perform command: " + error);
        }
    };

    @Inject
    public PerformCommand(Log log,
                          ListenersFactory listenersFactory,
                          @Assisted TaskData data,
                          @Assisted RealTaskOwner owner,
                          ObjectRoot<?, ?> root, RealObjectType<BaseHousemateObject<?>> realObjectType) {
        super(log, listenersFactory, "perform-command", data, owner);
        commandPath = new RealProperty<>(log, listenersFactory,
                "command-path", "Command Path", "The path to the command to perform", realObjectType, (List)null);
        getProperties().add(commandPath);
        addPropertyListener(root);
    }

    private void addPropertyListener(final ObjectRoot<?, ?> root) {
        commandPath.addObjectListener(new Property.Listener<RealProperty<RealObjectType.Reference<BaseHousemateObject<?>>>>() {

            @Override
            public void valueChanging(RealProperty<RealObjectType.Reference<BaseHousemateObject<?>>> value) {
                if(commandLifecycleListenerRegistration != null)
                    commandLifecycleListenerRegistration.removeListener();
            }

            @Override
            public void valueChanged(RealProperty<RealObjectType.Reference<BaseHousemateObject<?>>> property) {
                String[] path = property.getTypedValue().getPath();
                commandLifecycleListenerRegistration = root.addObjectLifecycleListener(path, PerformCommand.this);
                BaseHousemateObject<?> object = root.getObject(path);
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
    public void objectCreated(String[] path, BaseHousemateObject<?> object) {
        if(!(object instanceof Command))
            setError("Object at path " + Joiner.on("/").join(commandPath.getTypedValue().getPath()) + " is not a command");
        else {
            setError(null);
            command = (Command<TypeInstanceMap, ?, ?, ?>)object;
        }
    }

    @Override
    public void objectRemoved(String[] path, BaseHousemateObject<?> object) {
        command = null;
        setError("Cannot find an object at path " + Joiner.on("/").join(commandPath.getTypedValue().getPath()));
    }

    @Override
    public void execute() {
        if(command != null) {
            getLog().w("Executing " + Joiner.on("/").join(commandPath.getTypedValue().getPath()));
            command.perform(new TypeInstanceMap(), listener);
        } else
            getLog().w("Cannot execute task, no command at " + Joiner.on("/").join(commandPath.getTypedValue().getPath()));
    }
}

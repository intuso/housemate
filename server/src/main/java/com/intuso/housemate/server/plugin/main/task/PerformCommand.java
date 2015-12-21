package com.intuso.housemate.server.plugin.main.task;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.real.impl.internal.type.RealObjectType;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.utilities.listener.ListenerRegistration;
import org.slf4j.Logger;

/**
 */
@TypeInfo(id = "perform-command", name = "Perform Command", description = "Perform a command in the system")
public class PerformCommand implements TaskDriver, ObjectLifecycleListener {

    private RealObjectType.Reference<BaseHousemateObject<?>> commandPath;

    private final Logger logger;
    private final ObjectRoot objectRoot;
    private final TaskDriver.Callback callback;
    
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
            callback.setError("Failed to perform command: " + error);
        }
    };

    @Inject
    public PerformCommand(ObjectRoot objectRoot,
                          @Assisted Logger logger,
                          @Assisted TaskDriver.Callback callback) {
        this.logger = logger;
        this.objectRoot = objectRoot;
        this.callback = callback;
        String[] path = commandPath != null ? commandPath.getPath() : null;
        if(path != null)
            commandLifecycleListenerRegistration = this.objectRoot.addObjectLifecycleListener(path, PerformCommand.this);
    }

    @com.intuso.housemate.client.real.api.internal.annotations.Property("object")
    @TypeInfo(id = "command-path", name = "Command Path", description = "The path to the command to perform")
    public void setCommandPath(RealObjectType.Reference<BaseHousemateObject<?>> commandPath) {
        if(commandLifecycleListenerRegistration != null)
            commandLifecycleListenerRegistration.removeListener();
        this.commandPath = commandPath;
        if(commandPath != null) {
            String[] path = commandPath.getPath();
            commandLifecycleListenerRegistration = objectRoot.addObjectLifecycleListener(path, PerformCommand.this);
            BaseHousemateObject<?> object = objectRoot.findObject(path);
            if (object == null)
                callback.setError("Cannot find an object at path " + Joiner.on("/").join(path));
            else {
                objectCreated(path, object);
            }
        }
    }

    @Override
    public void objectCreated(String[] path, BaseHousemateObject<?> object) {
        if(!(object instanceof Command))
            callback.setError("Object at path " + Joiner.on("/").join(commandPath.getPath()) + " is not a command");
        else {
            callback.setError(null);
            command = (Command<TypeInstanceMap, ?, ?, ?>)object;
        }
    }

    @Override
    public void objectRemoved(String[] path, BaseHousemateObject<?> object) {
        command = null;
        callback.setError("Cannot find an object at path " + Joiner.on("/").join(commandPath.getPath()));
    }

    @Override
    public void execute() {
        if(command != null) {
            logger.warn("Executing " + Joiner.on("/").join(commandPath.getPath()));
            command.perform(new TypeInstanceMap(), listener);
        } else
            logger.warn("Cannot execute task, no command at " + Joiner.on("/").join(commandPath.getPath()));
    }
}

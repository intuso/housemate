package com.intuso.housemate.broker.object.real.consequence;

import com.google.common.base.Joiner;
import com.intuso.housemate.broker.object.bridge.BridgeObject;
import com.intuso.housemate.broker.object.bridge.CommandBridge;
import com.intuso.housemate.broker.object.real.BrokerRealProperty;
import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.HousemateObject;
import com.intuso.housemate.core.object.ObjectLifecycleListener;
import com.intuso.housemate.core.object.command.Command;
import com.intuso.housemate.core.object.command.CommandListener;
import com.intuso.housemate.core.object.value.ValueListener;
import com.intuso.housemate.real.impl.type.RealObjectType;
import com.intuso.listeners.ListenerRegistration;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/08/12
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */
public class PerformCommand extends BrokerRealConsequence implements ObjectLifecycleListener {

    private final BrokerRealProperty<RealObjectType.Reference<BridgeObject<?, ?, ?, ?, ?>>> commandPath;
    private CommandBridge command;
    private ListenerRegistration<ObjectLifecycleListener> commandLifecycleListenerRegistration = null;

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

    public PerformCommand(BrokerRealResources resources, String id, String name, String description) {
        super(resources, id, name, description);
        commandPath = new BrokerRealProperty<RealObjectType.Reference<BridgeObject<?, ?, ?, ?, ?>>>(resources, "command-path", "Command Path", "The path to the command to perform",
                new RealObjectType(resources.getGeneralResources().getClientResources(), resources.getGeneralResources().getBridgeResources().getRoot()), null);
        getProperties().add(commandPath);
        addPropertyListener();
    }

    private void addPropertyListener() {
        commandPath.addObjectListener(new ValueListener<BrokerRealProperty<RealObjectType.Reference<BridgeObject<?, ?, ?, ?, ?>>>>() {
            @Override
            public void valueChanged(BrokerRealProperty<RealObjectType.Reference<BridgeObject<?, ?, ?, ?, ?>>> property) {
                if(commandLifecycleListenerRegistration != null)
                    commandLifecycleListenerRegistration.removeListener();
                String[] path = property.getTypedValue().getPath();
                commandLifecycleListenerRegistration = getResources().getGeneralResources().getBridgeResources().getRoot().addObjectLifecycleListener(path, PerformCommand.this);
                HousemateObject<?, ?, ?, ?, ?> object = getResources().getGeneralResources().getBridgeResources().getRoot().getWrapper(path);
                if(object == null)
                    setError("Cannot find an object at path " + commandPath.getValue());
                else {
                    objectCreated(path, object);
                }
            }
        });
        String[] path = commandPath.getTypedValue().getPath();
        if(path != null)
            commandLifecycleListenerRegistration = getResources().getGeneralResources().getBridgeResources().getRoot().addObjectLifecycleListener(path, PerformCommand.this);
    }

    @Override
    public void objectCreated(String[] path, HousemateObject<?, ?, ?, ?, ?> object) {
        if(!(object instanceof CommandBridge))
            setError("Object at path " + commandPath.getTypedValue() + " is not a command");
        else {
            setError(null);
            command = (CommandBridge)object;
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
            command.perform(new HashMap<String, String>(), listener);
        } else
            getLog().w("Cannot execute consequence, no command at " + Joiner.on("/").join(commandPath.getTypedValue().getPath()));
    }
}

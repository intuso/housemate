package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.comms.api.internal.payload.TaskData;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.Task;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class TaskBridge
        extends BridgeObject<
        TaskData,
        HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>,
            TaskBridge,
            Task.Listener<? super TaskBridge>>
        implements Task<
            CommandBridge,
        ValueBridge,
        ValueBridge,
        PropertyBridge,
            ValueBridge,
        ConvertingListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>,
            TaskBridge> {

    private CommandBridge removeCommand;
    private ValueBridge executingValue;
    private ValueBridge errorValue;
    private PropertyBridge driverProperty;
    private ValueBridge driverLoadedValue;
    private ConvertingListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> propertyList;

    public TaskBridge(Log log, ListenersFactory listenersFactory, Task<?, ?, ?, ?, ?, ?, ?> task) {
        super(log, listenersFactory, new TaskData(task.getId(), task.getName(), task.getDescription()));
        removeCommand = new CommandBridge(log, listenersFactory, task.getRemoveCommand());
        executingValue = new ValueBridge(log, listenersFactory, task.getExecutingValue());
        errorValue = new ValueBridge(log, listenersFactory, task.getErrorValue());
        driverProperty = new PropertyBridge(log, listenersFactory, task.getDriverProperty());
        driverLoadedValue = new ValueBridge(log, listenersFactory, task.getDriverLoadedValue());
        propertyList = new ConvertingListBridge<>(log, listenersFactory, task.getProperties(),
                new PropertyBridge.Converter(log, listenersFactory));
        addChild(removeCommand);
        addChild(executingValue);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
        addChild(propertyList);
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public PropertyBridge getDriverProperty() {
        return driverProperty;
    }

    @Override
    public ValueBridge getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public ValueBridge getExecutingValue() {
        return executingValue;
    }

    @Override
    public ConvertingListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    public static class Converter implements Function<Task<?, ?, ?, ?, ?, ?, ?>, TaskBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public TaskBridge apply(Task<?, ?, ?, ?, ?, ?, ?> command) {
            return new TaskBridge(log, listenersFactory, command);
        }
    }
}

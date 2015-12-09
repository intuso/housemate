package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.comms.api.internal.payload.TaskData;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.Task;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

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

    public TaskBridge(Logger logger, ListenersFactory listenersFactory, Task<?, ?, ?, ?, ?, ?, ?> task) {
        super(logger, listenersFactory, new TaskData(task.getId(), task.getName(), task.getDescription()));
        removeCommand = new CommandBridge(logger, listenersFactory, task.getRemoveCommand());
        executingValue = new ValueBridge(logger, listenersFactory, task.getExecutingValue());
        errorValue = new ValueBridge(logger, listenersFactory, task.getErrorValue());
        driverProperty = new PropertyBridge(logger, listenersFactory, task.getDriverProperty());
        driverLoadedValue = new ValueBridge(logger, listenersFactory, task.getDriverLoadedValue());
        propertyList = new ConvertingListBridge<>(logger, listenersFactory, task.getProperties(),
                new PropertyBridge.Converter(logger, listenersFactory));
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

        private final Logger logger;
        private final ListenersFactory listenersFactory;

        public Converter(Logger logger, ListenersFactory listenersFactory) {
            this.logger = logger;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public TaskBridge apply(Task<?, ?, ?, ?, ?, ?, ?> command) {
            return new TaskBridge(logger, listenersFactory, command);
        }
    }
}

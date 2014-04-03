package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.task.TaskListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
public class TaskBridge
        extends BridgeObject<
            TaskData,
            HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>,
            TaskBridge,
            TaskListener<? super TaskBridge>>
        implements Task<
            CommandBridge,
            ValueBridge,
            ValueBridge,
            ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>,
            TaskBridge> {

    private CommandBridge removeCommand;
    private ValueBridge executingValue;
    private ValueBridge errorValue;
    private ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> propertyList;

    public TaskBridge(Log log, ListenersFactory listenersFactory, Task<?, ?, ?, ?, ?> task,
                      ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, listenersFactory, new TaskData(task.getId(), task.getName(), task.getDescription()));
        removeCommand = new CommandBridge(log, listenersFactory, task.getRemoveCommand(), types);
        executingValue = new ValueBridge(log, listenersFactory, task.getExecutingValue(), types);
        errorValue = new ValueBridge(log, listenersFactory, task.getErrorValue(), types);
        propertyList = new SingleListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>(log, listenersFactory, task.getProperties(),
                new PropertyBridge.Converter(log, listenersFactory, types));
        addChild(removeCommand);
        addChild(executingValue);
        addChild(errorValue);
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
    public String getError() {
        List<String> errors = RealType.deserialiseAll(StringType.SERIALISER, errorValue.getTypeInstances());
        return errors != null && errors.size() > 0 ? errors.get(0) : null;
    }

    @Override
    public ValueBridge getExecutingValue() {
        return executingValue;
    }

    @Override
    public boolean isExecuting() {
        List<Boolean> executings = RealType.deserialiseAll(BooleanType.SERIALISER, executingValue.getTypeInstances());
        return executings != null && executings.size() > 0 && executings.get(0) != null ? executings.get(0) : false;
    }

    @Override
    public ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    public static class Converter implements Function<Task<?, ?, ?, ?, ?>, TaskBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(Log log, ListenersFactory listenersFactory, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.log = log;
            this.listenersFactory = listenersFactory;
            this.types = types;;
        }

        @Override
        public TaskBridge apply(Task<?, ?, ?, ?, ?> command) {
            return new TaskBridge(log, listenersFactory, command, types);
        }
    }
}

package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskListener;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

import javax.annotation.Nullable;

/**
 */
public class TaskBridge
        extends BridgeObject<TaskWrappable, HousemateObjectWrappable<?>, BridgeObject<?, ?, ?, ?, ?>, TaskBridge,
        TaskListener<? super TaskBridge>>
        implements Task<ValueBridge, ValueBridge,
                            ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge>,
        TaskBridge> {

    private ValueBridge executingValue;
    private ValueBridge errorValue;
    private ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge> propertyList;

    public TaskBridge(BrokerBridgeResources resources, Task<?, ?, ?, ?> task) {
        super(resources, new TaskWrappable(task.getId(), task.getName(), task.getDescription()));
        executingValue = new ValueBridge(resources,task.getExecutingValue());
        errorValue = new ValueBridge(resources,task.getErrorValue());
        propertyList = new ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge>(resources, task.getProperties(), new PropertyBridge.Converter(resources));
        addWrapper(executingValue);
        addWrapper(errorValue);
        addWrapper(propertyList);
    }

    @Override
    public ValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public String getError() {
        return StringType.SERIALISER.deserialise(errorValue.getTypeInstance());
    }

    @Override
    public ValueBridge getExecutingValue() {
        return executingValue;
    }

    @Override
    public boolean isExecuting() {
        Boolean value = BooleanType.SERIALISER.deserialise(executingValue.getTypeInstance());
        return value != null ? value : false;
    }

    @Override
    public ListBridge<PropertyWrappable, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    public static class Converter implements Function<Task<?, ?, ?, ?>, TaskBridge> {

        private final BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public TaskBridge apply(@Nullable Task<?, ?, ?, ?> command) {
            return new TaskBridge(resources, command);
        }
    }
}

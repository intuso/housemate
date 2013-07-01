package com.intuso.housemate.sample.plugin.task;

import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.plugin.api.BrokerTaskFactory;

/**
 * Example factory for tasks with a non-simple constructor. This factory has a simple
 * constructor so can be used with
 * {@link com.intuso.housemate.annotations.plugin.TaskFactories} annotations
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class ComplexTaskFactory implements BrokerTaskFactory<ComplexTask> {

    private final Object simpleArg = new Object();

    @Override
    public String getTypeId() {
        return "complex-task-factory";
    }

    @Override
    public String getTypeName() {
        return "Complex Task Factory";
    }

    @Override
    public String getTypeDescription() {
        return "A factory for complex tasks";
    }

    @Override
    public ComplexTask create(BrokerRealResources resources, String id, String name, String description) {
        return new ComplexTask(resources, id, name, description, simpleArg);
    }
}

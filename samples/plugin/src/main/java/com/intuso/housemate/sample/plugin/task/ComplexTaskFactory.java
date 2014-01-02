package com.intuso.housemate.sample.plugin.task;

import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.plugin.api.ServerTaskFactory;

/**
 * Example factory for tasks with a non-simple constructor. This factory has a simple
 * constructor so can be used with
 * {@link com.intuso.housemate.annotations.plugin.TaskFactories} annotations
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class ComplexTaskFactory implements ServerTaskFactory<ComplexTask> {

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
    public ComplexTask create(ServerRealResources resources, String id, String name, String description, ServerRealTaskOwner owner) {
        return new ComplexTask(resources, id, name, description, owner, simpleArg);
    }
}

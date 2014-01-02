package com.intuso.housemate.sample.plugin.task;

import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.plugin.api.ServerTaskFactory;

/**
 * Example factory for tasks with a non-simple constructor. This factory also has a non-simple
 * constructor so must be manually added by overriding
 * {@link com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor#getTaskFactories()}
 * and adding an instance of this to the resulting list
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class ReallyComplexTaskFactory implements ServerTaskFactory<ComplexTask> {

    private final Object complexArg;

    public ReallyComplexTaskFactory(Object complexArg) {
        this.complexArg = complexArg;
    }

    @Override
    public String getTypeId() {
        return "really-complex-task-factory";
    }

    @Override
    public String getTypeName() {
        return "Really Complex Task Factory";
    }

    @Override
    public String getTypeDescription() {
        return "A complex factory for complex tasks";
    }

    @Override
    public ComplexTask create(ServerRealResources resources, String id, String name, String description,
                              ServerRealTaskOwner owner) {
        return new ComplexTask(resources, id, name, description, owner, complexArg);
    }
}

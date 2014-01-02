package com.intuso.housemate.sample.plugin.task;

import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;

/**
 * Complex task with a non-simple constructor that cannot be used with the
 * {@link com.intuso.housemate.annotations.plugin.Tasks} annotation so requires a factory
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 * @see com.intuso.housemate.sample.plugin.task.ComplexTaskFactory
 * @see com.intuso.housemate.sample.plugin.task.ReallyComplexTaskFactory
 */
public class ComplexTask extends ServerRealTask {

    private final Object extraArg;

    public ComplexTask(ServerRealResources resources, String id, String name, String description,
                       ServerRealTaskOwner owner, Object extraArg) {
        super(resources, id, name, description, owner);
        this.extraArg = extraArg;
    }

    @Override
    public void execute() {}
}

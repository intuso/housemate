package com.intuso.housemate.sample.plugin.task;

import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.utilities.log.Log;

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

    public ComplexTask(Log log, String id, String name, String description,
                       ServerRealTaskOwner owner, Object extraArg) {
        super(log, id, name, description, owner);
        this.extraArg = extraArg;
    }

    @Override
    public void execute() {}
}

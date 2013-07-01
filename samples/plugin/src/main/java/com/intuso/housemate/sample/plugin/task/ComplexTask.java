package com.intuso.housemate.sample.plugin.task;

import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealTask;

/**
 * Complex task with a non-simple constructor that cannot be used with the
 * {@link com.intuso.housemate.annotations.plugin.Tasks} annotation so requires a factory
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 * @see com.intuso.housemate.sample.plugin.task.ComplexTaskFactory
 * @see com.intuso.housemate.sample.plugin.task.ReallyComplexTaskFactory
 */
public class ComplexTask extends BrokerRealTask {

    private final Object extraArg;

    public ComplexTask(BrokerRealResources resources, String id, String name, String description, Object extraArg) {
        super(resources, id, name, description);
        this.extraArg = extraArg;
    }

    @Override
    public void execute() {}
}

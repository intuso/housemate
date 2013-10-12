package com.intuso.housemate.sample.plugin.task;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealTask;
import com.intuso.housemate.object.broker.real.BrokerRealTaskOwner;

/**
 * Example task with a simple constructor that can be used with the
 * {@link com.intuso.housemate.annotations.plugin.Tasks} annotation
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class DoYourThingTask extends BrokerRealTask {

    public DoYourThingTask(BrokerRealResources resources, String id, String name, String description, BrokerRealTaskOwner owner) {
        super(resources, id, name, description, owner);
    }

    @Override
    protected void execute() throws HousemateException {
        // do whatever thing it is you do
    }
}

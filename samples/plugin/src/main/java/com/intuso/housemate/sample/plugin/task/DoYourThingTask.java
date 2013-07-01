package com.intuso.housemate.sample.plugin.task;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealTask;

/**
 * Example task with a simple constructor that can be used with the
 * {@link com.intuso.housemate.annotations.plugin.Tasks} annotation
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class DoYourThingTask extends BrokerRealTask {

    public final static String ID = "your-thing";
    public final static String NAME = "Your Thing";
    public final static String DESCRIPTION = "Do something special that only you can do!";

    public DoYourThingTask(BrokerRealResources resources) {
        super(resources, ID, NAME, DESCRIPTION);
    }

    @Override
    protected void execute() throws HousemateException {
        // do whatever thing it is you do
    }
}

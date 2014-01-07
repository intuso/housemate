package com.intuso.housemate.sample.plugin.task;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.utilities.log.Log;

/**
 * Example task with a simple constructor that can be used with the
 * {@link com.intuso.housemate.annotations.plugin.Tasks} annotation
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class DoYourThingTask extends ServerRealTask {

    public DoYourThingTask(Log log, String id, String name, String description, ServerRealTaskOwner owner) {
        super(log, id, name, description, owner);
    }

    @Override
    protected void execute() throws HousemateException {
        // do whatever thing it is you do
    }
}

package com.intuso.housemate.sample.plugin.task;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

@TypeInfo(id = "do-your-thing", name = "Do Your Thing", description = "Special task that does your thing")
public class DoYourThingTask extends ServerRealTask {

    @Inject
    public DoYourThingTask(Log log,
                           ListenersFactory listenersFactory,
                           @Assisted TaskData data,
                           @Assisted ServerRealTaskOwner owner) {
        super(log, listenersFactory, data, owner);
    }

    @Override
    protected void execute() throws HousemateException {
        // do whatever thing it is you do
    }
}

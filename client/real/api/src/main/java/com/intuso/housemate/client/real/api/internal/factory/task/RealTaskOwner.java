package com.intuso.housemate.client.real.api.internal.factory.task;

import com.intuso.housemate.client.real.api.internal.RealTask;
import com.intuso.housemate.comms.api.internal.ChildOverview;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 12/10/13
* Time: 22:37
* To change this template use File | Settings | File Templates.
*/
public interface RealTaskOwner {
    ChildOverview getAddTaskCommandDetails();
    void addTask(RealTask task);
    void removeTask(RealTask task);
}

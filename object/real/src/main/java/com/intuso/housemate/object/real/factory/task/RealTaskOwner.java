package com.intuso.housemate.object.real.factory.task;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.object.real.RealTask;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 12/10/13
* Time: 22:37
* To change this template use File | Settings | File Templates.
*/
public interface RealTaskOwner {
    public ChildOverview getAddTaskCommandDetails();
    public void addTask(RealTask task);
    public void removeTask(RealTask task);
}

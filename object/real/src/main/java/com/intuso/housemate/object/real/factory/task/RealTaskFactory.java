package com.intuso.housemate.object.real.factory.task;

import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.real.RealTask;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealTaskFactory<TASK extends RealTask> {
    public TASK create(TaskData data, RealTaskOwner owner);
}

package com.intuso.housemate.client.real.api.internal.factory.task;

import com.intuso.housemate.client.real.api.internal.RealTask;
import com.intuso.housemate.comms.api.internal.payload.TaskData;

/**
 * Created by tomc on 20/03/15.
 */
public interface RealTaskFactory<TASK extends RealTask> {
    public TASK create(TaskData data, RealTaskOwner owner);
}

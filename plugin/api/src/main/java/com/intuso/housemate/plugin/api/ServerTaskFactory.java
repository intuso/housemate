package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;

/**
 * @param <TASK> the type of the tasks created by this factory
 */
public interface ServerTaskFactory<TASK extends ServerRealTask> {

    /**
     * Creates a task
     * @param data the task's data
     * @param owner the owner that will be called if the task is deleted
     * @return a new task
     */
    public TASK create(TaskData data, ServerRealTaskOwner owner);
}

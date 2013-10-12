package com.intuso.housemate.api.object.task;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of tasks
 */
public interface HasTasks<L extends List<? extends Task<?, ?, ?, ?, ?>>> {

    /**
     * Gets the task list
     * @return the task list
     */
    public L getTasks();
}

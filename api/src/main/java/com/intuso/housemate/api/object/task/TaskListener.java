package com.intuso.housemate.api.object.task;

import com.intuso.housemate.api.object.ObjectListener;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 04/03/12
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public interface TaskListener<T extends Task<?, ?, ?, ?>>
        extends ObjectListener {

    /**
     * Called when a task starts/stops executing
     * @param task the task that has started/stopped execution
     * @param executing true if the condition is now executing
     */
    public void taskExecuting(T task, boolean executing);

    /**
     * Called when a task is in error (or not)
     * @param task the task that is in error (or not)
     * @param error description of the error or null if not in error
     */
    public void taskError(T task, String error);
}

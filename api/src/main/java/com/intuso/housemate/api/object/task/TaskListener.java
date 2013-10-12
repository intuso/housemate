package com.intuso.housemate.api.object.task;

import com.intuso.housemate.api.object.ObjectListener;

/**
 *
 * Listener interface for tasks
 */
public interface TaskListener<T extends Task<?, ?, ?, ?, ?>>
        extends ObjectListener {

    /**
     * Notifies that a task starts/stops executing
     * @param task the task that has started/stopped execution
     * @param executing true if the task is now executing
     */
    public void taskExecuting(T task, boolean executing);

    /**
     * Notifies that a task is in error (or not)
     * @param task the task that is in error (or not)
     * @param error the description of the error or null if not in error
     */
    public void taskError(T task, String error);
}

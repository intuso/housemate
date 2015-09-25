package com.intuso.housemate.object.api.internal;

/**
 * @param <EXECUTING_VALUE> the type of the executing value
 * @param <ERROR_VALUE> the type of the error value
 * @param <PROPERTIES> the type of the properties list
 * @param <TASK> the type of the task
 */
public interface Task<
            REMOVE_COMMAND extends Command<?, ?, ?, ?>,
            EXECUTING_VALUE extends Value<?, ?>,
            ERROR_VALUE extends Value<?, ?>,
            PROPERTIES extends List<? extends Property<?, ?, ?>>,
            TASK extends Task<REMOVE_COMMAND, EXECUTING_VALUE, ERROR_VALUE, PROPERTIES, TASK>>
        extends BaseHousemateObject<Task.Listener<? super TASK>>,
        Property.Container<PROPERTIES>,
        Removeable<REMOVE_COMMAND>,
        Failable<ERROR_VALUE> {

    /**
     * Gets the executing value object
     * @return the executing value object
     */
    EXECUTING_VALUE getExecutingValue();

    /**
     *
     * Listener interface for tasks
     */
    interface Listener<T extends Task<?, ?, ?, ?, ?>>
            extends ObjectListener {

        /**
         * Notifies that a task starts/stops executing
         * @param task the task that has started/stopped execution
         * @param executing true if the task is now executing
         */
        void taskExecuting(T task, boolean executing);

        /**
         * Notifies that a task is in error (or not)
         * @param task the task that is in error (or not)
         * @param error the description of the error or null if not in error
         */
        void taskError(T task, String error);
    }

    /**
     *
     * Interface to show that the implementing object has a list of tasks
     */
    interface Container<TASKS extends List<? extends Task<?, ?, ?, ?, ?>>> {

        /**
         * Gets the task list
         * @return the task list
         */
        TASKS getTasks();
    }
}

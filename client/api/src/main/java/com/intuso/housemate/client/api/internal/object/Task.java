package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.UsesDriver;

/**
 * @param <EXECUTING_VALUE> the type of the executing value
 * @param <ERROR_VALUE> the type of the error value
 * @param <PROPERTIES> the type of the properties list
 * @param <TASK> the type of the task
 */
public interface Task<REMOVE_COMMAND extends Command<?, ?, ?, ?>,
        RENAME_COMMAND extends Command<?, ?, ?, ?>,
        ERROR_VALUE extends Value<?, ?, ?>,
        DRIVER_PROPERTY extends Property<?, ?, ?, ?>,
        DRIVER_LOADED_VALUE extends Value<?, ?, ?>,
        EXECUTING_VALUE extends Value<?, ?, ?>,
        PROPERTIES extends List<? extends Property<?, ?, ?, ?>, ?>,
        TASK extends Task<REMOVE_COMMAND, RENAME_COMMAND, ERROR_VALUE, DRIVER_PROPERTY, DRIVER_LOADED_VALUE, EXECUTING_VALUE, PROPERTIES, TASK>>
        extends Object<Task.Listener<? super TASK>>,
        Property.Container<PROPERTIES>,
        Renameable<RENAME_COMMAND>,
        Removeable<REMOVE_COMMAND>,
        Failable<ERROR_VALUE>,
        UsesDriver<DRIVER_PROPERTY, DRIVER_LOADED_VALUE> {

    String EXECUTING_ID = "executing";
    String PROPERTIES_ID = "property";

    /**
     * Gets the executing value object
     * @return the executing value object
     */
    EXECUTING_VALUE getExecutingValue();

    /**
     *
     * Listener interface for tasks
     */
    interface Listener<TASK extends Task<?, ?, ?, ?, ?, ?, ?, ?>> extends Object.Listener,
            Renameable.Listener<TASK>,
            Failable.Listener<TASK>,
            UsesDriver.Listener<TASK> {

        /**
         * Notifies that a task starts/stops executing
         * @param task the task that has started/stopped execution
         * @param executing true if the task is now executing
         */
        void taskExecuting(TASK task, boolean executing);
    }

    /**
     *
     * Interface to show that the implementing object has a list of tasks
     */
    interface Container<TASKS extends Iterable<? extends Task<?, ?, ?, ?, ?, ?, ?, ?>>> {

        /**
         * Gets the task list
         * @return the task list
         */
        TASKS getTasks();
    }

    /**
     *
     * Data object for a task
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "task";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
        }
    }
}

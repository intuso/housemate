package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.view.AutomationView;

/**
 * @param <RENAME_COMMAND> the type of the command for renaming the automation
 * @param <REMOVE_COMMAND> the type of the command for removing the automation
 * @param <START_STOP_COMMAND> the type of the command for stopping or starting
 * @param <ADD_COMMMAND> the type of the command for adding new conditions or tasks
 * @param <RUNNING_VALUE> the type of the running value
 * @param <ERROR_VALUE> the type of the error value
 * @param <CONDITIONS> the type of the conditions list
 * @param <TASKS> the type of the tasks list
 * @param <AUTOMATION> the type of the automation
 */
public interface Automation<RENAME_COMMAND extends Command<?, ?, ?, ?>,
        REMOVE_COMMAND extends Command<?, ?, ?, ?>,
        RUNNING_VALUE extends Value<?, ?, ?>,
        START_STOP_COMMAND extends Command<?, ?, ?, ?>,
        ERROR_VALUE extends Value<?, ?, ?>,
        ADD_COMMMAND extends Command<?, ?, ?, ?>,
        CONDITIONS extends List<? extends Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        TASKS extends List<? extends Task<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        AUTOMATION extends Automation<RENAME_COMMAND, REMOVE_COMMAND, RUNNING_VALUE, START_STOP_COMMAND, ERROR_VALUE, ADD_COMMMAND, CONDITIONS, TASKS, AUTOMATION>>
        extends
        Object<Automation.Data, Automation.Listener<? super AUTOMATION>, AutomationView>,
        Renameable<RENAME_COMMAND>,
        Runnable<START_STOP_COMMAND, RUNNING_VALUE>,
        Failable<ERROR_VALUE>,
        Removeable<REMOVE_COMMAND>,
        Condition.Container<CONDITIONS> {

    String CONDITIONS_ID = "condition";
    String SATISFIED_TASKS_ID = "satisfied-task";
    String UNSATISFIED_TASKS_ID = "unsatisfied-task";
    String ADD_CONDITION_ID = "add-condition";
    String ADD_SATISFIED_TASK_ID = "add-satisfied-task";
    String ADD_UNSATISFIED_TASK_ID = "add-unsatisfied-task";

    /**
     * Gets the satisfied tasks list
     * @return the satisfied tasks list
     */
    TASKS getSatisfiedTasks();

    /**
     * Gets the unsatisfied tasks list
     * @return the unsatisfied tasks list
     */
    TASKS getUnsatisfiedTasks();

    /**
     * Gets the add condition command
     * @return the add condition command
     */
    ADD_COMMMAND getAddConditionCommand();

    /**
     * Gets the add satisfied task command
     * @return the add satisfied task  command
     */
    ADD_COMMMAND getAddSatisfiedTaskCommand();

    /**
     * Gets the add unsatisfied task command
     * @return the add unsatisfied task  command
     */
    ADD_COMMMAND getAddUnsatisfiedTaskCommand();

    /**
     * Listener interface for automations
     */
    interface Listener<AUTOMATION extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?>>
            extends Object.Listener,
            Failable.Listener<AUTOMATION>,
            Renameable.Listener<AUTOMATION>,
            Runnable.Listener<AUTOMATION> {

        /**
         * Notifies that the automation's root condition is (un)satisfied
         * @param automation the automation that became (un)satisfied
         * @param satisfied true if now satisfied
         */
        void satisfied(AUTOMATION automation, boolean satisfied);
    }

    /**
     * Interface to show that the implementing object has a list of automations
     */
    interface Container<AUTOMATIONS extends Iterable<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?>>> {

        /**
         * Gets the automation list
         * @return the automation list
         */
        AUTOMATIONS getAutomations();
    }

    /**
     * Data object for an automation
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "automation";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_CLASS, id, name,  description);
        }
    }
}

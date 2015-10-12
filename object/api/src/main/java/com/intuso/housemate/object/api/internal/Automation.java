package com.intuso.housemate.object.api.internal;

/**
 * @param <RENAME_COMMAND> the type of the command for renaming the automation
 * @param <REMOVE_COMMAND> the type of the command for removing the automation
 * @param <START_STOP_COMMAND> the type of the command for stopping or starting
 * @param <ADD_COMMMAND> the type of the command for adding new conditions or tasks
 * @param <RUNNING_VALUE> the type of the running value
 * @param <ERROR_VALUE> the type of the error value
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 * @param <TASK> the type of the tasks
 * @param <TASKS> the type of the tasks list
 * @param <AUTOMATION> the type of the automation
 */
public interface Automation<RENAME_COMMAND extends Command<?, ?, ?, ?>,
        REMOVE_COMMAND extends Command<?, ?, ?, ?>,
        START_STOP_COMMAND extends Command<?, ?, ?, ?>,
        ADD_COMMMAND extends Command<?, ?, ?, ?>,
        RUNNING_VALUE extends Value<?, ?>,
        ERROR_VALUE extends Value<?, ?>,
        CONDITION extends Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>,
        CONDITIONS extends List<? extends CONDITION>,
        TASK extends Task<?, ?, ?, ?, ?, ?, ?>,
        TASKS extends List<? extends TASK>,
        AUTOMATION extends Automation<RENAME_COMMAND, REMOVE_COMMAND, START_STOP_COMMAND, ADD_COMMMAND, RUNNING_VALUE, ERROR_VALUE, CONDITION, CONDITIONS, TASK, TASKS, AUTOMATION>>
        extends
        BaseHousemateObject<Automation.Listener<? super AUTOMATION>>,
        Renameable<RENAME_COMMAND>,
        Runnable<START_STOP_COMMAND, RUNNING_VALUE>,
        Failable<ERROR_VALUE>,
        Removeable<REMOVE_COMMAND>,
        Condition.Container<CONDITIONS> {

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
    ADD_COMMMAND getAddSatisifedTaskCommand();

    /**
     * Gets the add unsatisfied task command
     * @return the add unsatisfied task  command
     */
    ADD_COMMMAND getAddUnsatisifedTaskCommand();

    /**
     * Listener interface for automations
     */
    interface Listener<AUTOMATION extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
            extends ObjectListener,
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
    interface Container<AUTOMATIONS extends List<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>> {

        /**
         * Gets the automation list
         * @return the automation list
         */
        AUTOMATIONS getAutomations();
    }
}

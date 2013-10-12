package com.intuso.housemate.api.object.automation;

import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.HasConditions;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.value.Value;

/**
 * @param <REMOVE_COMMAND> the type of the command for removing the automation
 * @param <START_STOP_COMMAND> the type of the command for stopping or starting
 * @param <ADD_COMMMAND> the type of the command for adding new conditions or tasks
 * @param <RUNNING_VALUE> the type of the running value
 * @param <ERROR_VALUE> the type of the error value
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 * @param <TASK> the type of the tasks
 * @param <TASKS> the type of the tasks list
 * @param <A> the type of the automation
 */
public interface Automation<
            REMOVE_COMMAND extends Command<?, ?>,
            START_STOP_COMMAND extends Command<?, ?>,
            ADD_COMMMAND extends Command<?, ?>,
            RUNNING_VALUE extends Value<?, ?>,
            ERROR_VALUE extends Value<?, ?>,
            CONDITION extends Condition<?, ?, ?, ?, ?, ?, ?>,
            CONDITIONS extends List<? extends CONDITION>,
            TASK extends Task<?, ?, ?, ?, ?>,
            TASKS extends List<? extends TASK>,
            A extends Automation<REMOVE_COMMAND, START_STOP_COMMAND, ADD_COMMMAND, RUNNING_VALUE, ERROR_VALUE, CONDITION, CONDITIONS, TASK, TASKS, A>>
        extends PrimaryObject<REMOVE_COMMAND, START_STOP_COMMAND, RUNNING_VALUE, ERROR_VALUE, A, AutomationListener<? super A>>, HasConditions<CONDITIONS> {

    public final static String CONDITIONS_ID = "conditions";
    public final static String SATISFIED_TASKS_ID = "satisfied-tasks";
    public final static String UNSATISFIED_TASKS_ID = "unsatisfied-tasks";
    public final static String ADD_CONDITION_ID = "add-condition";
    public final static String ADD_SATISFIED_TASK_ID = "add-satisfied-task";
    public final static String ADD_UNSATISFIED_TASK_ID = "add-unsatisfied-task";

    /**
     * Gets the satisfied tasks list
     * @return the satisfied tasks list
     */
    public TASKS getSatisfiedTasks();

    /**
     * Gets the unsatisfied tasks list
     * @return the unsatisfied tasks list
     */
    public TASKS getUnsatisfiedTasks();

    /**
     * Gets the add condition command
     * @return the add condition command
     */
    public ADD_COMMMAND getAddConditionCommand();

    /**
     * Gets the add satisfied task command
     * @return the add satisfied task  command
     */
    public ADD_COMMMAND getAddSatisifedTaskCommand();

    /**
     * Gets the add unsatisfied task command
     * @return the add unsatisfied task  command
     */
    public ADD_COMMMAND getAddUnsatisifedTaskCommand();
}

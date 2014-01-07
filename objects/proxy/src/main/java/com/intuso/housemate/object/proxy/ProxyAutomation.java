package com.intuso.housemate.object.proxy;

import com.google.inject.Injector;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.AutomationListener;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.utilities.log.Log;

/**
 * @param <ADD_COMMAND> the type of the add command
 * @param <VALUE> the type of the value
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 * @param <TASK> the type of the tasks
 * @param <TASKS> the type of the tasks list
 * @param <AUTOMATION> the type of the automation
 */
public abstract class ProxyAutomation<
            ADD_COMMAND extends ProxyCommand<?, ?, ADD_COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            CONDITION extends ProxyCondition<?, ?, ?, CONDITION, CONDITIONS>,
            CONDITIONS extends ProxyList<ConditionData, CONDITION, CONDITIONS>,
            TASK extends ProxyTask<?, ?, ?, TASK>,
            TASKS extends ProxyList<TaskData, TASK, TASKS>,
            AUTOMATION extends ProxyAutomation<ADD_COMMAND, VALUE, CONDITION, CONDITIONS, TASK, TASKS, AUTOMATION>>
        extends ProxyPrimaryObject<AutomationData, ADD_COMMAND, VALUE, AUTOMATION, AutomationListener<? super AUTOMATION>>
        implements Automation<ADD_COMMAND, ADD_COMMAND, ADD_COMMAND, VALUE, VALUE, CONDITION, CONDITIONS, TASK, TASKS, AUTOMATION> {

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyAutomation(Log log, Injector injector, AutomationData data) {
        super(log, injector, data);
    }

    @Override
    public CONDITIONS getConditions() {
        return (CONDITIONS) getChild(CONDITIONS_ID);
    }

    @Override
    public TASKS getSatisfiedTasks() {
        return (TASKS) getChild(SATISFIED_TASKS_ID);
    }

    @Override
    public TASKS getUnsatisfiedTasks() {
        return (TASKS) getChild(UNSATISFIED_TASKS_ID);
    }

    @Override
    public ADD_COMMAND getAddConditionCommand() {
        return (ADD_COMMAND) getChild(ADD_CONDITION_ID);
    }

    @Override
    public ADD_COMMAND getAddSatisifedTaskCommand() {
        return (ADD_COMMAND) getChild(ADD_SATISFIED_TASK_ID);
    }

    @Override
    public ADD_COMMAND getAddUnsatisifedTaskCommand() {
        return (ADD_COMMAND) getChild(ADD_UNSATISFIED_TASK_ID);
    }
}

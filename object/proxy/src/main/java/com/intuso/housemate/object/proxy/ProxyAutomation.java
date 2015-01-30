package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.AutomationListener;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <COMMAND> the type of the add command
 * @param <VALUE> the type of the value
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 * @param <TASK> the type of the tasks
 * @param <TASKS> the type of the tasks list
 * @param <AUTOMATION> the type of the automation
 */
public abstract class ProxyAutomation<
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            CONDITION extends ProxyCondition<?, ?, ?, CONDITION, CONDITIONS>,
            CONDITIONS extends ProxyList<ConditionData, CONDITION, CONDITIONS>,
            TASK extends ProxyTask<?, ?, ?, TASK>,
            TASKS extends ProxyList<TaskData, TASK, TASKS>,
            AUTOMATION extends ProxyAutomation<COMMAND, VALUE, CONDITION, CONDITIONS, TASK, TASKS, AUTOMATION>>
        extends ProxyPrimaryObject<AutomationData, COMMAND, VALUE, AUTOMATION, AutomationListener<? super AUTOMATION>>
        implements Automation<COMMAND, COMMAND, COMMAND, COMMAND, VALUE, VALUE, CONDITION, CONDITIONS, TASK, TASKS, AUTOMATION> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyAutomation(Log log, ListenersFactory listenersFactory, AutomationData data) {
        super(log, listenersFactory, data);
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
    public COMMAND getAddConditionCommand() {
        return (COMMAND) getChild(ADD_CONDITION_ID);
    }

    @Override
    public COMMAND getAddSatisifedTaskCommand() {
        return (COMMAND) getChild(ADD_SATISFIED_TASK_ID);
    }

    @Override
    public COMMAND getAddUnsatisifedTaskCommand() {
        return (COMMAND) getChild(ADD_UNSATISFIED_TASK_ID);
    }
}

package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationListener;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <ADD_COMMAND> the type of the add command
 * @param <VALUE> the type of the value
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 * @param <TASK> the type of the tasks
 * @param <TASKS> the type of the tasks list
 * @param <AUTOMATION> the type of the automation
 */
public abstract class ProxyAutomation<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            ADD_COMMAND extends ProxyCommand<?, ?, ?, ?, ADD_COMMAND>,
            VALUE extends ProxyValue<?, ?, VALUE>,
            CONDITION extends ProxyCondition<?, ?, ?, ?, ?, CONDITION, CONDITIONS>,
            CONDITIONS extends ProxyList<?, ?, ConditionData, CONDITION, CONDITIONS>,
            TASK extends ProxyTask<?, ?, ?, ?, TASK>,
            TASKS extends ProxyList<?, ?, TaskData, TASK, TASKS>,
            AUTOMATION extends ProxyAutomation<RESOURCES, CHILD_RESOURCES, ADD_COMMAND, VALUE, CONDITION, CONDITIONS, TASK, TASKS, AUTOMATION>>
        extends ProxyPrimaryObject<RESOURCES, CHILD_RESOURCES, AutomationData, ADD_COMMAND, VALUE, AUTOMATION, AutomationListener<? super AUTOMATION>>
        implements Automation<ADD_COMMAND, ADD_COMMAND, ADD_COMMAND, VALUE, VALUE, VALUE, CONDITION, CONDITIONS, TASK, TASKS, AUTOMATION> {

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyAutomation(RESOURCES resources, CHILD_RESOURCES childResources, AutomationData data) {
        super(resources, childResources, data);
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

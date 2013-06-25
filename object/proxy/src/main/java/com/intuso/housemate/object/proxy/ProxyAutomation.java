package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.task.TaskWrappable;
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
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            ADD_COMMAND extends ProxyCommand<?, ?, ?, ?, ADD_COMMAND>,
            VALUE extends ProxyValue<?, ?, VALUE>,
            CONDITION extends ProxyCondition<?, ?, ?, ?, ?, CONDITION, CONDITIONS>,
            CONDITIONS extends ProxyList<?, ?, ConditionWrappable, CONDITION, CONDITIONS>,
            TASK extends ProxyTask<?, ?, ?, ?, TASK>,
            TASKS extends ProxyList<?, ?, TaskWrappable, TASK, TASKS>,
            AUTOMATION extends ProxyAutomation<RESOURCES, CHILD_RESOURCES, ADD_COMMAND, VALUE, CONDITION, CONDITIONS, TASK, TASKS, AUTOMATION>>
        extends ProxyPrimaryObject<RESOURCES, CHILD_RESOURCES, AutomationWrappable, ADD_COMMAND, VALUE, AUTOMATION, AutomationListener<? super AUTOMATION>>
        implements Automation<ADD_COMMAND, ADD_COMMAND, ADD_COMMAND, VALUE, VALUE, VALUE, CONDITION, CONDITIONS, TASK, TASKS, AUTOMATION> {

    private CONDITIONS conditions;
    private TASKS satisfiedTasks;
    private TASKS unsatisfiedTasks;
    private ADD_COMMAND addConditionCommand;
    private ADD_COMMAND addSatisifedTaskCommand;
    private ADD_COMMAND addUnsatisifedTaskCommand;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param wrappable {@inheritDoc}
     */
    public ProxyAutomation(RESOURCES resources, CHILD_RESOURCES childResources, AutomationWrappable wrappable) {
        super(resources, childResources, wrappable);
    }

    @Override
    protected final void getChildObjects() {
        super.getChildObjects();
        conditions = (CONDITIONS)getWrapper(CONDITIONS_ID);
        satisfiedTasks = (TASKS)getWrapper(SATISFIED_TASKS_ID);
        unsatisfiedTasks = (TASKS)getWrapper(UNSATISFIED_TASKS_ID);
        addConditionCommand = (ADD_COMMAND)getWrapper(ADD_CONDITION_ID);
        addSatisifedTaskCommand = (ADD_COMMAND)getWrapper(ADD_SATISFIED_TASK_ID);
        addUnsatisifedTaskCommand = (ADD_COMMAND)getWrapper(ADD_UNSATISFIED_TASK_ID);
    }

    @Override
    public CONDITIONS getConditions() {
        return conditions;
    }

    @Override
    public TASKS getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public TASKS getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }

    @Override
    public ADD_COMMAND getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public ADD_COMMAND getAddSatisifedTaskCommand() {
        return addSatisifedTaskCommand;
    }

    @Override
    public ADD_COMMAND getAddUnsatisifedTaskCommand() {
        return addUnsatisifedTaskCommand;
    }
}

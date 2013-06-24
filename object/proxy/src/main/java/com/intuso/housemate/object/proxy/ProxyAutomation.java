package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationListener;

/**
 */
public abstract class ProxyAutomation<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SR extends ProxyResources<?>,
            AC extends ProxyCommand<?, ?, ?, ?, AC>,
            V extends ProxyValue<?, ?, V>,
            C extends ProxyCondition<?, ?, ?, ?, ?, C, CL>,
            CL extends ProxyList<?, ?, ConditionWrappable, C, CL>,
            T extends ProxyTask<?, ?, ?, ?, T>,
            TL extends ProxyList<?, ?, TaskWrappable, T, TL>,
            A extends ProxyAutomation<R, SR, AC, V, C, CL, T, TL, A>>
        extends ProxyPrimaryObject<R, SR, AutomationWrappable, AC, V, A, AutomationListener<? super A>>
        implements Automation<AC, AC, AC, V, V, V, C, CL, T, TL, A> {

    private CL conditions;
    private TL satisfiedTasks;
    private TL unsatisfiedTasks;
    private AC addConditionCommand;
    private AC addSatisifedTaskCommand;
    private AC addUnsatisifedTaskCommand;

    public ProxyAutomation(R resources, SR subResources, AutomationWrappable wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected final void getChildObjects() {
        super.getChildObjects();
        conditions = (CL)getWrapper(CONDITIONS_ID);
        satisfiedTasks = (TL)getWrapper(SATISFIED_TASKS_ID);
        unsatisfiedTasks = (TL)getWrapper(UNSATISFIED_TASKS_ID);
        addConditionCommand = (AC)getWrapper(ADD_CONDITION_ID);
        addSatisifedTaskCommand = (AC)getWrapper(ADD_SATISFIED_TASK_ID);
        addUnsatisifedTaskCommand = (AC)getWrapper(ADD_UNSATISFIED_TASK_ID);
    }

    @Override
    public CL getConditions() {
        return conditions;
    }

    public TL getSatisfiedTasks() {
        return satisfiedTasks;
    }

    public TL getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }

    public AC getAddConditionCommand() {
        return addConditionCommand;
    }

    public AC getAddSatisifedTaskCommand() {
        return addSatisifedTaskCommand;
    }

    public AC getAddUnsatisifedTaskCommand() {
        return addUnsatisifedTaskCommand;
    }
}

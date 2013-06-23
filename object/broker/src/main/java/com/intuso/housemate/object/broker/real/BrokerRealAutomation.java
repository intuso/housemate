package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationListener;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/05/12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public class BrokerRealAutomation
        extends BrokerRealPrimaryObject<AutomationWrappable, BrokerRealAutomation, AutomationListener<? super BrokerRealAutomation>>
        implements Automation<BrokerRealProperty<String>, BrokerRealCommand, BrokerRealCommand, BrokerRealCommand,
                    BrokerRealValue<Boolean>, BrokerRealValue<Boolean>, BrokerRealValue<String>, BrokerRealCondition, BrokerRealList<ConditionWrappable, BrokerRealCondition>,
        BrokerRealTask, BrokerRealList<TaskWrappable, BrokerRealTask>, BrokerRealAutomation>,
            ConditionListener<BrokerRealCondition> {

    private BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions;
    private BrokerRealList<TaskWrappable, BrokerRealTask> satisfiedTasks;
    private BrokerRealList<TaskWrappable, BrokerRealTask> unsatisfiedTasks;
    private BrokerRealCommand addConditionCommand;
    private BrokerRealCommand addSatisfiedTaskCommand;
    private BrokerRealCommand addUnsatisfiedTaskCommand;

    private ListenerRegistration conditionListenerRegistration;

    public BrokerRealAutomation(final BrokerRealResources resources, String id, String name, String description) {
        super(resources, new AutomationWrappable(id, name, description), "automation");
        this.conditions = new BrokerRealList<ConditionWrappable, BrokerRealCondition>(resources, CONDITIONS, CONDITIONS, "The automation's conditions");
        this.satisfiedTasks = new BrokerRealList<TaskWrappable, BrokerRealTask>(resources, SATISFIED_TASKS, SATISFIED_TASKS, "The tasks to run when the automation is satisfied");
        this.unsatisfiedTasks = new BrokerRealList<TaskWrappable, BrokerRealTask>(resources, UNSATISFIED_TASKS, UNSATISFIED_TASKS, "The tasks to run when the automation is satisfied");
        addConditionCommand = getResources().getLifecycleHandler().createAddConditionCommand(conditions);
        addSatisfiedTaskCommand = getResources().getLifecycleHandler().createAddSatisfiedTaskCommand(satisfiedTasks);
        addUnsatisfiedTaskCommand = getResources().getLifecycleHandler().createAddUnsatisfiedTaskCommand(unsatisfiedTasks);
        addWrapper(conditions);
        addWrapper(satisfiedTasks);
        addWrapper(unsatisfiedTasks);
        addWrapper(addConditionCommand);
        addWrapper(addSatisfiedTaskCommand);
        addWrapper(addUnsatisfiedTaskCommand);
    }

    @Override
    protected void remove() {
        getResources().getRoot().getAutomations().remove(getId());
        getResources().getLifecycleHandler().automationRemoved(getPath());
    }

    @Override
    public BrokerRealList<TaskWrappable, BrokerRealTask> getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public BrokerRealList<TaskWrappable, BrokerRealTask> getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }

    @Override
    public BrokerRealCommand getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public BrokerRealCommand getAddSatisifedTaskCommand() {
        return addSatisfiedTaskCommand;
    }

    @Override
    public BrokerRealCommand getAddUnsatisifedTaskCommand() {
        return addUnsatisfiedTaskCommand;
    }

    @Override
    public BrokerRealList<ConditionWrappable, BrokerRealCondition> getConditions() {
        return conditions;
    }

    @Override
    public void conditionError(BrokerRealCondition condition, String error) {
        // do nothing for now
    }

    @Override
    public void conditionSatisfied(BrokerRealCondition condition, boolean satisfied) {
        try {
            getLog().d("Automation " + (satisfied ? "" : "un") + "satisfied, executing tasks");
            for(BrokerRealTask task : (satisfied ? satisfiedTasks : unsatisfiedTasks))
                task.execute();
        } catch (HousemateException e) {
            getErrorValue().setTypedValue("Failed to perform automation tasks: " + e.getMessage());
            getLog().e("Failed to perform automation tasks");
            getLog().st(e);
        }
    }

    public void start() throws HousemateException {
        if(conditions.size() == 0)
            throw new HousemateException("Automation has no condition. It must have exactly one");
        else if(conditions.size() > 1)
            throw new HousemateException(("Automation has multiple conditions. It can only have one"));
        else {
            BrokerRealCondition condition = conditions.iterator().next();
            condition.start();
            conditionListenerRegistration = condition.addObjectListener(this);
        }
    }

    public void stop() {
        for(BrokerRealCondition condition : conditions)
            condition.stop();
        if(conditionListenerRegistration != null) {
            conditionListenerRegistration.removeListener();
            conditionListenerRegistration = null;
        }
    }
}

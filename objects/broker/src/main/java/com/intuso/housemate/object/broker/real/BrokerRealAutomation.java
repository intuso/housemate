package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.AutomationListener;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.utilities.listener.ListenerRegistration;

public class BrokerRealAutomation
        extends BrokerRealPrimaryObject<
            AutomationData,
            BrokerRealAutomation,
            AutomationListener<? super BrokerRealAutomation>>
        implements Automation<
            BrokerRealCommand, BrokerRealCommand, BrokerRealCommand,
            BrokerRealValue<Boolean>, BrokerRealValue<String>, BrokerRealCondition, BrokerRealList<ConditionData, BrokerRealCondition>,
            BrokerRealTask, BrokerRealList<TaskData, BrokerRealTask>, BrokerRealAutomation>,
            ConditionListener<BrokerRealCondition>,
        BrokerRealConditionOwner {

    private BrokerRealList<ConditionData, BrokerRealCondition> conditions;
    private BrokerRealList<TaskData, BrokerRealTask> satisfiedTasks;
    private BrokerRealList<TaskData, BrokerRealTask> unsatisfiedTasks;
    private BrokerRealCommand addConditionCommand;
    private BrokerRealCommand addSatisfiedTaskCommand;
    private BrokerRealCommand addUnsatisfiedTaskCommand;

    private final BrokerRealTaskOwner satisfiedTaskOwner = new BrokerRealTaskOwner() {
        @Override
        public void remove(BrokerRealTask task) {
            satisfiedTasks.remove(task.getId());
        }
    };
    private final BrokerRealTaskOwner unsatisfiedTaskOwner = new BrokerRealTaskOwner() {
        @Override
        public void remove(BrokerRealTask task) {
            unsatisfiedTasks.remove(task.getId());
        }
    };

    private ListenerRegistration conditionListenerRegistration;

    public BrokerRealAutomation(final BrokerRealResources resources, String id, String name, String description) {
        super(resources, new AutomationData(id, name, description), "automation");
        this.conditions = new BrokerRealList<ConditionData, BrokerRealCondition>(resources, CONDITIONS_ID, CONDITIONS_ID, "The automation's conditions");
        this.satisfiedTasks = new BrokerRealList<TaskData, BrokerRealTask>(resources, SATISFIED_TASKS_ID, SATISFIED_TASKS_ID, "The tasks to run when the automation is satisfied");
        this.unsatisfiedTasks = new BrokerRealList<TaskData, BrokerRealTask>(resources, UNSATISFIED_TASKS_ID, UNSATISFIED_TASKS_ID, "The tasks to run when the automation is satisfied");
        addConditionCommand = getResources().getLifecycleHandler().createAddConditionCommand(conditions, this);
        addSatisfiedTaskCommand = getResources().getLifecycleHandler().createAddSatisfiedTaskCommand(satisfiedTasks, satisfiedTaskOwner);
        addUnsatisfiedTaskCommand = getResources().getLifecycleHandler().createAddUnsatisfiedTaskCommand(unsatisfiedTasks, unsatisfiedTaskOwner);
        addChild(conditions);
        addChild(satisfiedTasks);
        addChild(unsatisfiedTasks);
        addChild(addConditionCommand);
        addChild(addSatisfiedTaskCommand);
        addChild(addUnsatisfiedTaskCommand);
    }

    @Override
    protected void remove() {
        getResources().getRoot().getAutomations().remove(getId());
        getResources().getLifecycleHandler().automationRemoved(getPath());
    }

    @Override
    public BrokerRealList<TaskData, BrokerRealTask> getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public BrokerRealList<TaskData, BrokerRealTask> getUnsatisfiedTasks() {
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
    public BrokerRealList<ConditionData, BrokerRealCondition> getConditions() {
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
                task.executeTask();
        } catch (HousemateException e) {
            getErrorValue().setTypedValue("Failed to perform automation tasks: " + e.getMessage());
            getLog().e("Failed to perform automation tasks");
            getLog().st(e);
        }
    }

    @Override
    public void remove(BrokerRealCondition condition) {
        conditions.remove(condition.getId());
    }

    public BrokerRealTaskOwner getSatisfiedTaskOwner() {
        return satisfiedTaskOwner;
    }

    public BrokerRealTaskOwner getUnsatisfiedTaskOwner() {
        return unsatisfiedTaskOwner;
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

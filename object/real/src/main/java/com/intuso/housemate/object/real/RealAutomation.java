package com.intuso.housemate.object.real;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.AutomationListener;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.real.factory.automation.RealAutomationOwner;
import com.intuso.housemate.object.real.factory.condition.AddConditionCommand;
import com.intuso.housemate.object.real.factory.condition.RealConditionOwner;
import com.intuso.housemate.object.real.factory.task.AddTaskCommand;
import com.intuso.housemate.object.real.factory.task.RealTaskOwner;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class RealAutomation
        extends RealPrimaryObject<
            AutomationData,
            RealAutomation,
            AutomationListener<? super RealAutomation>>
        implements Automation<
            RealCommand, RealCommand, RealCommand, RealCommand,
            RealValue<Boolean>, RealValue<String>, RealCondition, RealList<ConditionData, RealCondition>,
            RealTask, RealList<TaskData, RealTask>, RealAutomation>,
            ConditionListener<RealCondition>,
        RealConditionOwner {

    private final RealList<ConditionData, RealCondition> conditions;
    private final RealList<TaskData, RealTask> satisfiedTasks;
    private final RealList<TaskData, RealTask> unsatisfiedTasks;
    private final RealCommand addConditionCommand;
    private final RealCommand addSatisfiedTaskCommand;
    private final RealCommand addUnsatisfiedTaskCommand;

    private final RealAutomationOwner owner;

    private final RealTaskOwner satisfiedTaskOwner = new RealTaskOwner() {

        @Override
        public ChildOverview getAddTaskCommandDetails() {
            return new ChildOverview(ADD_SATISFIED_TASK_ID, ADD_SATISFIED_TASK_ID, "Add satisfied task");
        }

        @Override
        public void addTask(RealTask task) {
            satisfiedTasks.add(task);
        }

        @Override
        public void removeTask(RealTask task) {
            satisfiedTasks.remove(task.getId());
        }
    };

    private final RealTaskOwner unsatisfiedTaskOwner = new RealTaskOwner() {

        @Override
        public ChildOverview getAddTaskCommandDetails() {
            return new ChildOverview(ADD_UNSATISFIED_TASK_ID, ADD_UNSATISFIED_TASK_ID, "Add unsatisfied task");
        }

        @Override
        public void addTask(RealTask task) {
            unsatisfiedTasks.add(task);
        }

        @Override
        public void removeTask(RealTask task) {
            unsatisfiedTasks.remove(task.getId());
        }
    };

    private ListenerRegistration conditionListenerRegistration;

    @Inject
    public RealAutomation(final Log log, ListenersFactory listenersFactory,
                          AddConditionCommand.Factory addConditionCommandFactory, AddTaskCommand.Factory addTaskCommandFactory,
                          @Assisted AutomationData data, @Assisted RealAutomationOwner owner) {
        super(log, listenersFactory, data, "automation");
        this.owner = owner;
        this.conditions = new RealList<ConditionData, RealCondition>(log, listenersFactory, CONDITIONS_ID, CONDITIONS_ID, "The automation's conditions");
        this.satisfiedTasks = new RealList<TaskData, RealTask>(log, listenersFactory, SATISFIED_TASKS_ID, SATISFIED_TASKS_ID, "The tasks to run when the automation is satisfied");
        this.unsatisfiedTasks = new RealList<TaskData, RealTask>(log, listenersFactory, UNSATISFIED_TASKS_ID, UNSATISFIED_TASKS_ID, "The tasks to run when the automation is satisfied");
        addConditionCommand = addConditionCommandFactory.create(this);
        addSatisfiedTaskCommand = addTaskCommandFactory.create(satisfiedTaskOwner);
        addUnsatisfiedTaskCommand = addTaskCommandFactory.create(unsatisfiedTaskOwner);
        addChild(conditions);
        addChild(satisfiedTasks);
        addChild(unsatisfiedTasks);
        addChild(addConditionCommand);
        addChild(addSatisfiedTaskCommand);
        addChild(addUnsatisfiedTaskCommand);
    }

    @Override
    protected void remove() {
        owner.removeAutomation(this);
    }

    @Override
    public RealList<TaskData, RealTask> getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public RealList<TaskData, RealTask> getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }

    @Override
    public RealCommand getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public RealCommand getAddSatisifedTaskCommand() {
        return addSatisfiedTaskCommand;
    }

    @Override
    public RealCommand getAddUnsatisifedTaskCommand() {
        return addUnsatisfiedTaskCommand;
    }

    @Override
    public RealList<ConditionData, RealCondition> getConditions() {
        return conditions;
    }

    @Override
    public void conditionError(RealCondition condition, String error) {
        // do nothing for now
    }

    @Override
    public void conditionSatisfied(RealCondition condition, boolean satisfied) {
        try {
            getLog().d("Automation " + (satisfied ? "" : "un") + "satisfied, executing tasks");
            for(RealTask task : (satisfied ? satisfiedTasks : unsatisfiedTasks))
                task.executeTask();
        } catch (HousemateException e) {
            getErrorValue().setTypedValues("Failed to perform automation tasks: " + e.getMessage());
            getLog().e("Failed to perform automation tasks", e);
        }
    }

    @Override
    public ChildOverview getAddConditionCommandDetails() {
        return new ChildOverview(ADD_CONDITION_ID, ADD_CONDITION_ID, "Add condition");
    }

    @Override
    public void addCondition(RealCondition condition) {
        conditions.add(condition);
    }

    @Override
    public void removeCondition(RealCondition condition) {
        conditions.remove(condition.getId());
    }

    public RealTaskOwner getSatisfiedTaskOwner() {
        return satisfiedTaskOwner;
    }

    public RealTaskOwner getUnsatisfiedTaskOwner() {
        return unsatisfiedTaskOwner;
    }

    @Override
     protected final void _start() {
        try {
            start();
        } catch (HousemateException e) {
            getErrorValue().setTypedValues("Could not start device: " + e.getMessage());
        }
    }

    @Override
    protected final void _stop() {
        stop();
    }

    public void start() throws HousemateException {
        if(conditions.size() == 0)
            throw new HousemateException("Automation has no condition. It must have exactly one");
        else if(conditions.size() > 1)
            throw new HousemateException(("Automation has multiple conditions. It can only have one"));
        else {
            RealCondition condition = conditions.iterator().next();
            condition.start();
            conditionListenerRegistration = condition.addObjectListener(this);
            conditionSatisfied(condition, condition.isSatisfied());
        }
    }

    public void stop() {
        for(RealCondition condition : conditions)
            condition.stop();
        if(conditionListenerRegistration != null) {
            conditionListenerRegistration.removeListener();
            conditionListenerRegistration = null;
        }
    }

}

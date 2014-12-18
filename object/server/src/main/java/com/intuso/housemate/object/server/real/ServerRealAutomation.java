package com.intuso.housemate.object.server.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.AutomationListener;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionListener;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerRealAutomation
        extends ServerRealPrimaryObject<
                    AutomationData,
        ServerRealAutomation,
                    AutomationListener<? super ServerRealAutomation>>
        implements Automation<
        ServerRealCommand, ServerRealCommand, ServerRealCommand,
        ServerRealValue<Boolean>, ServerRealValue<String>, ServerRealCondition, ServerRealList<ConditionData, ServerRealCondition>,
        ServerRealTask, ServerRealList<TaskData, ServerRealTask>, ServerRealAutomation>,
            ConditionListener<ServerRealCondition>,
        ServerRealConditionOwner {

    private final ServerRealList<ConditionData, ServerRealCondition> conditions;
    private final ServerRealList<TaskData, ServerRealTask> satisfiedTasks;
    private final ServerRealList<TaskData, ServerRealTask> unsatisfiedTasks;
    private final ServerRealCommand addConditionCommand;
    private final ServerRealCommand addSatisfiedTaskCommand;
    private final ServerRealCommand addUnsatisfiedTaskCommand;

    private final ServerRealAutomationOwner owner;

    private final ServerRealTaskOwner satisfiedTaskOwner = new ServerRealTaskOwner() {
        @Override
        public void remove(ServerRealTask task) {
            satisfiedTasks.remove(task.getId());
        }
    };
    private final ServerRealTaskOwner unsatisfiedTaskOwner = new ServerRealTaskOwner() {
        @Override
        public void remove(ServerRealTask task) {
            unsatisfiedTasks.remove(task.getId());
        }
    };

    private ListenerRegistration conditionListenerRegistration;

    public ServerRealAutomation(final Log log, ListenersFactory listenersFactory, String id, String name, String description,
                                ServerRealAutomationOwner owner, LifecycleHandler lifecycleHandler) {
        super(log, listenersFactory, new AutomationData(id, name, description), "automation");
        this.owner = owner;
        this.conditions = new ServerRealList<ConditionData, ServerRealCondition>(log, listenersFactory, CONDITIONS_ID, CONDITIONS_ID, "The automation's conditions");
        this.satisfiedTasks = new ServerRealList<TaskData, ServerRealTask>(log, listenersFactory, SATISFIED_TASKS_ID, SATISFIED_TASKS_ID, "The tasks to run when the automation is satisfied");
        this.unsatisfiedTasks = new ServerRealList<TaskData, ServerRealTask>(log, listenersFactory, UNSATISFIED_TASKS_ID, UNSATISFIED_TASKS_ID, "The tasks to run when the automation is satisfied");
        addConditionCommand = lifecycleHandler.createAddConditionCommand(conditions, this);
        addSatisfiedTaskCommand = lifecycleHandler.createAddSatisfiedTaskCommand(satisfiedTasks, satisfiedTaskOwner);
        addUnsatisfiedTaskCommand = lifecycleHandler.createAddUnsatisfiedTaskCommand(unsatisfiedTasks, unsatisfiedTaskOwner);
        addChild(conditions);
        addChild(satisfiedTasks);
        addChild(unsatisfiedTasks);
        addChild(addConditionCommand);
        addChild(addSatisfiedTaskCommand);
        addChild(addUnsatisfiedTaskCommand);
    }

    @Override
    protected void remove() {
        owner.remove(this);
    }

    @Override
    public ServerRealList<TaskData, ServerRealTask> getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public ServerRealList<TaskData, ServerRealTask> getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }

    @Override
    public ServerRealCommand getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public ServerRealCommand getAddSatisifedTaskCommand() {
        return addSatisfiedTaskCommand;
    }

    @Override
    public ServerRealCommand getAddUnsatisifedTaskCommand() {
        return addUnsatisfiedTaskCommand;
    }

    @Override
    public ServerRealList<ConditionData, ServerRealCondition> getConditions() {
        return conditions;
    }

    @Override
    public void conditionError(ServerRealCondition condition, String error) {
        // do nothing for now
    }

    @Override
    public void conditionSatisfied(ServerRealCondition condition, boolean satisfied) {
        try {
            getLog().d("Automation " + (satisfied ? "" : "un") + "satisfied, executing tasks");
            for(ServerRealTask task : (satisfied ? satisfiedTasks : unsatisfiedTasks))
                task.executeTask();
        } catch (HousemateException e) {
            getErrorValue().setTypedValue("Failed to perform automation tasks: " + e.getMessage());
            getLog().e("Failed to perform automation tasks", e);
        }
    }

    @Override
    public void remove(ServerRealCondition condition) {
        conditions.remove(condition.getId());
    }

    public ServerRealTaskOwner getSatisfiedTaskOwner() {
        return satisfiedTaskOwner;
    }

    public ServerRealTaskOwner getUnsatisfiedTaskOwner() {
        return unsatisfiedTaskOwner;
    }

    public void start() throws HousemateException {
        if(conditions.size() == 0)
            throw new HousemateException("Automation has no condition. It must have exactly one");
        else if(conditions.size() > 1)
            throw new HousemateException(("Automation has multiple conditions. It can only have one"));
        else {
            ServerRealCondition condition = conditions.iterator().next();
            condition.start();
            conditionListenerRegistration = condition.addObjectListener(this);
            conditionSatisfied(condition, condition.isSatisfied());
        }
    }

    public void stop() {
        for(ServerRealCondition condition : conditions)
            condition.stop();
        if(conditionListenerRegistration != null) {
            conditionListenerRegistration.removeListener();
            conditionListenerRegistration = null;
        }
    }
}

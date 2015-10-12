package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.real.api.internal.factory.task.AddTaskCommand;
import com.intuso.housemate.client.real.api.internal.impl.type.BooleanType;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.Automation;
import com.intuso.housemate.object.api.internal.Condition;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class RealAutomation
        extends RealObject<
        AutomationData,
        HousemateData<?>,
        RealObject<?, ?, ?, ?>,
        Automation.Listener<? super RealAutomation>>
        implements Automation<
        RealCommand, RealCommand, RealCommand, RealCommand,
        RealValue<Boolean>, RealValue<String>, RealCondition<?>, RealList<ConditionData, RealCondition<?>>,
        RealTask<?>, RealList<TaskData, RealTask<?>>, RealAutomation>,
        Condition.Listener<RealCondition<?>>,
        RealCondition.RemovedListener,
        AddConditionCommand.Callback {

    private final RealCommand rename;
    private final RealCommand remove;
    private final RealValue<Boolean> running;
    private final RealCommand start;
    private final RealCommand stop;
    private final RealValue<String> error;
    private final RealList<ConditionData, RealCondition<?>> conditions;
    private final RealList<TaskData, RealTask<?>> satisfiedTasks;
    private final RealList<TaskData, RealTask<?>> unsatisfiedTasks;
    private final RealCommand addConditionCommand;
    private final RealCommand addSatisfiedTaskCommand;
    private final RealCommand addUnsatisfiedTaskCommand;

    private final RemovedListener removedListener;

    private final AddTaskCommand.Callback addSatisfiedTaskCallback = new AddTaskCommand.Callback() {

        @Override
        public void addTask(RealTask task) {
            satisfiedTasks.add(task);
        }
    };

    private final RealTask.RemovedListener satisfiedTaskRemovedListener = new RealTask.RemovedListener() {

        @Override
        public void taskRemoved(RealTask task) {
            satisfiedTasks.remove(task.getId());
        }
    };

    private final AddTaskCommand.Callback addUnsatisfiedTaskCallback = new AddTaskCommand.Callback() {

        @Override
        public void addTask(RealTask task) {
            unsatisfiedTasks.add(task);
        }
    };

    private final RealTask.RemovedListener unsatisfiedTaskRemovedListener = new RealTask.RemovedListener() {

        @Override
        public void taskRemoved(RealTask task) {
            unsatisfiedTasks.remove(task.getId());
        }
    };

    private ListenerRegistration conditionListenerRegistration;

    @Inject
    public RealAutomation(final Log log,
                          ListenersFactory listenersFactory,
                          AddConditionCommand.Factory addConditionCommandFactory,
                          AddTaskCommand.Factory addTaskCommandFactory,
                          @Assisted AutomationData data,
                          @Assisted RemovedListener removedListener) {
        super(log, listenersFactory, data);
        this.removedListener = removedListener;
        this.rename = new RealCommand(log, listenersFactory, AutomationData.RENAME_ID, AutomationData.RENAME_ID, "Rename the automation", Lists.<RealParameter<?>>newArrayList(StringType.createParameter(log, listenersFactory, AutomationData.NAME_ID, AutomationData.NAME_ID, "The new name"))) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(values != null && values.getChildren().containsKey(AutomationData.NAME_ID)) {
                    String newName = values.getChildren().get(AutomationData.NAME_ID).getFirstValue();
                    if (newName != null && !RealAutomation.this.getName().equals(newName)) {
                        RealAutomation.this.getData().setName(newName);
                        for(Automation.Listener<? super RealAutomation> listener : RealAutomation.this.getObjectListeners())
                            listener.renamed(RealAutomation.this, RealAutomation.this.getName(), newName);
                        RealAutomation.this.sendMessage(AutomationData.NEW_NAME, new StringPayload(newName));
                    }
                }
            }
        };
        this.remove = new RealCommand(log, listenersFactory, AutomationData.REMOVE_ID, AutomationData.REMOVE_ID, "Remove the automation", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning())
                    throw new HousemateCommsException("Cannot remove while automation is still running");
                remove();
            }
        };
        this.running = BooleanType.createValue(log, listenersFactory, AutomationData.RUNNING_ID, AutomationData.RUNNING_ID, "Whether the automation is running or not", false);
        this.start = new RealCommand(log, listenersFactory, AutomationData.START_ID, AutomationData.START_ID, "Start the automation", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(!isRunning()) {
                    _start();
                    running.setTypedValues(true);
                }
            }
        };
        this.stop = new RealCommand(log, listenersFactory, AutomationData.STOP_ID, AutomationData.STOP_ID, "Stop the automation", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning()) {
                    _stop();
                    running.setTypedValues(false);
                }
            }
        };
        this.error = StringType.createValue(log, listenersFactory, AutomationData.ERROR_ID, AutomationData.ERROR_ID, "Current error for the automation", null);
        this.conditions = new RealList<>(log, listenersFactory, AutomationData.CONDITIONS_ID, AutomationData.CONDITIONS_ID, "The automation's conditions");
        this.satisfiedTasks = new RealList<>(log, listenersFactory, AutomationData.SATISFIED_TASKS_ID, AutomationData.SATISFIED_TASKS_ID, "The tasks to run when the automation is satisfied");
        this.unsatisfiedTasks = new RealList<>(log, listenersFactory, AutomationData.UNSATISFIED_TASKS_ID, AutomationData.UNSATISFIED_TASKS_ID, "The tasks to run when the automation is satisfied");
        addConditionCommand = addConditionCommandFactory.create(AutomationData.ADD_CONDITION_ID, AutomationData.ADD_CONDITION_ID, "Add condition", this, this);
        addSatisfiedTaskCommand = addTaskCommandFactory.create(AutomationData.ADD_SATISFIED_TASK_ID, AutomationData.ADD_SATISFIED_TASK_ID, "Add satisfied task", addSatisfiedTaskCallback, satisfiedTaskRemovedListener);
        addUnsatisfiedTaskCommand = addTaskCommandFactory.create(AutomationData.ADD_UNSATISFIED_TASK_ID, AutomationData.ADD_UNSATISFIED_TASK_ID, "Add unsatisfied task", addUnsatisfiedTaskCallback, unsatisfiedTaskRemovedListener);
        addChild(this.rename);
        addChild(this.remove);
        addChild(this.running);
        addChild(this.start);
        addChild(this.stop);
        addChild(this.error);
        addChild(conditions);
        addChild(satisfiedTasks);
        addChild(unsatisfiedTasks);
        addChild(addConditionCommand);
        addChild(addSatisfiedTaskCommand);
        addChild(addUnsatisfiedTaskCommand);
    }

    @Override
    public RealCommand getRenameCommand() {
        return rename;
    }

    @Override
    public RealCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public RealValue<String> getErrorValue() {
        return error;
    }

    @Override
    public RealCommand getStopCommand() {
        return stop;
    }

    @Override
    public RealCommand getStartCommand() {
        return start;
    }

    @Override
    public RealValue<Boolean> getRunningValue() {
        return running;
    }

    public boolean isRunning() {
        return running.getTypedValue() != null ? running.getTypedValue() : false;
    }

    protected void remove() {
        removedListener.automationRemoved(this);
    }

    @Override
    public RealList<TaskData, RealTask<?>> getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public RealList<TaskData, RealTask<?>> getUnsatisfiedTasks() {
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
    public RealList<ConditionData, RealCondition<?>> getConditions() {
        return conditions;
    }

    @Override
    public void error(RealCondition condition, String error) {
        // do nothing for now
    }

    @Override
    public void driverLoaded(RealCondition usesDriver, boolean loaded) {
        // do nothing for now
    }

    @Override
    public void conditionSatisfied(RealCondition condition, boolean satisfied) {
        try {
            getLog().d("Automation " + (satisfied ? "" : "un") + "satisfied, executing tasks");
            for(RealTask task : (satisfied ? satisfiedTasks : unsatisfiedTasks))
                task.executeTask();
        } catch (Throwable t) {
            getErrorValue().setTypedValues("Failed to perform automation tasks: " + t.getMessage());
            getLog().e("Failed to perform automation tasks", t);
        }
    }

    @Override
    public void addCondition(RealCondition condition) {
        conditions.add(condition);
    }

    @Override
    public void conditionRemoved(RealCondition condition) {
        conditions.remove(condition.getId());
    }

    protected final void _start() {
        try {
            start();
        } catch (Throwable t) {
            getErrorValue().setTypedValues("Could not start automation: " + t.getMessage());
        }
    }

    protected final void _stop() {
        stop();
    }

    public void start() {
        if(conditions.size() == 0)
            throw new HousemateCommsException("Automation has no condition. It must have exactly one");
        else if(conditions.size() > 1)
            throw new HousemateCommsException(("Automation has multiple conditions. It can only have one"));
        else {
            RealCondition condition = conditions.iterator().next();
            condition.start();
            conditionListenerRegistration = condition.addObjectListener(this);
            conditionSatisfied(condition, condition.getChildSatisfied());
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

    public interface RemovedListener {
        void automationRemoved(RealAutomation automation);
    }

    public interface Factory {
        RealAutomation create(AutomationData data, RemovedListener removedListener);
    }
}
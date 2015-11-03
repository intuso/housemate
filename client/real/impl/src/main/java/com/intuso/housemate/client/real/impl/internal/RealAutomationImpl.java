package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.impl.internal.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.real.impl.internal.factory.task.AddTaskCommand;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.Automation;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class RealAutomationImpl
        extends RealObject<
        AutomationData,
        HousemateData<?>,
            RealObject<?, ?, ?, ?>,
            Automation.Listener<? super RealAutomation>>
        implements RealAutomation,
            AddConditionCommand.Callback {

    private final RealCommandImpl rename;
    private final RealCommandImpl remove;
    private final RealValueImpl<Boolean> running;
    private final RealCommandImpl start;
    private final RealCommandImpl stop;
    private final RealValueImpl<String> error;
    private final RealListImpl<ConditionData, RealConditionImpl<?>> conditions;
    private final RealListImpl<TaskData, RealTaskImpl<?>> satisfiedTasks;
    private final RealListImpl<TaskData, RealTaskImpl<?>> unsatisfiedTasks;
    private final RealCommandImpl addConditionCommand;
    private final RealCommandImpl addSatisfiedTaskCommand;
    private final RealCommandImpl addUnsatisfiedTaskCommand;

    private final RemovedListener removedListener;

    private final AddTaskCommand.Callback addSatisfiedTaskCallback = new AddTaskCommand.Callback() {

        @Override
        public void addTask(RealTask task) {
            satisfiedTasks.add((RealTaskImpl<?>) task);
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
            unsatisfiedTasks.add((RealTaskImpl<?>) task);
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
    public RealAutomationImpl(final Log log,
                              ListenersFactory listenersFactory,
                              AddConditionCommand.Factory addConditionCommandFactory,
                              AddTaskCommand.Factory addTaskCommandFactory,
                              @Assisted AutomationData data,
                              @Assisted RemovedListener removedListener) {
        super(log, listenersFactory, data);
        this.removedListener = removedListener;
        this.rename = new RealCommandImpl(log, listenersFactory, AutomationData.RENAME_ID, AutomationData.RENAME_ID, "Rename the automation", Lists.<RealParameterImpl<?>>newArrayList(StringType.createParameter(log, listenersFactory, AutomationData.NAME_ID, AutomationData.NAME_ID, "The new name"))) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(values != null && values.getChildren().containsKey(AutomationData.NAME_ID)) {
                    String newName = values.getChildren().get(AutomationData.NAME_ID).getFirstValue();
                    if (newName != null && !RealAutomationImpl.this.getName().equals(newName)) {
                        RealAutomationImpl.this.getData().setName(newName);
                        for(Automation.Listener<? super RealAutomationImpl> listener : RealAutomationImpl.this.getObjectListeners())
                            listener.renamed(RealAutomationImpl.this, RealAutomationImpl.this.getName(), newName);
                        RealAutomationImpl.this.sendMessage(AutomationData.NEW_NAME, new StringPayload(newName));
                    }
                }
            }
        };
        this.remove = new RealCommandImpl(log, listenersFactory, AutomationData.REMOVE_ID, AutomationData.REMOVE_ID, "Remove the automation", Lists.<RealParameterImpl<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning())
                    throw new HousemateCommsException("Cannot remove while automation is still running");
                remove();
            }
        };
        this.running = BooleanType.createValue(log, listenersFactory, AutomationData.RUNNING_ID, AutomationData.RUNNING_ID, "Whether the automation is running or not", false);
        this.start = new RealCommandImpl(log, listenersFactory, AutomationData.START_ID, AutomationData.START_ID, "Start the automation", Lists.<RealParameterImpl<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(!isRunning()) {
                    _start();
                    running.setTypedValues(true);
                }
            }
        };
        this.stop = new RealCommandImpl(log, listenersFactory, AutomationData.STOP_ID, AutomationData.STOP_ID, "Stop the automation", Lists.<RealParameterImpl<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning()) {
                    _stop();
                    running.setTypedValues(false);
                }
            }
        };
        this.error = StringType.createValue(log, listenersFactory, AutomationData.ERROR_ID, AutomationData.ERROR_ID, "Current error for the automation", null);
        this.conditions = new RealListImpl<>(log, listenersFactory, AutomationData.CONDITIONS_ID, AutomationData.CONDITIONS_ID, "The automation's conditions");
        this.satisfiedTasks = new RealListImpl<>(log, listenersFactory, AutomationData.SATISFIED_TASKS_ID, AutomationData.SATISFIED_TASKS_ID, "The tasks to run when the automation is satisfied");
        this.unsatisfiedTasks = new RealListImpl<>(log, listenersFactory, AutomationData.UNSATISFIED_TASKS_ID, AutomationData.UNSATISFIED_TASKS_ID, "The tasks to run when the automation is satisfied");
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

    private void remove() {
        removedListener.automationRemoved(this);
    }

    @Override
    public RealList<? extends RealTask<?>> getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public RealList<? extends RealTask<?>> getUnsatisfiedTasks() {
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
    public RealList<? extends RealCondition<?>> getConditions() {
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
        conditions.add((RealConditionImpl<?>) condition);
    }

    @Override
    public void conditionRemoved(RealCondition condition) {
        conditions.remove(condition.getId());
    }

    private void _start() {
        try {
            if(conditions.size() == 0)
                throw new HousemateCommsException("Automation has no condition. It must have exactly one");
            else if(conditions.size() > 1)
                throw new HousemateCommsException(("Automation has multiple conditions. It can only have one"));
            else {
                RealCondition condition = conditions.iterator().next();
                condition.start();
                conditionListenerRegistration = condition.addObjectListener(this);
                conditionSatisfied(condition, condition.isSatisfied());
            }
        } catch (Throwable t) {
            getErrorValue().setTypedValues("Could not start automation: " + t.getMessage());
        }
    }

    private void _stop() {
        for(RealCondition condition : conditions)
            condition.stop();
        if(conditionListenerRegistration != null) {
            conditionListenerRegistration.removeListener();
            conditionListenerRegistration = null;
        }
    }
}

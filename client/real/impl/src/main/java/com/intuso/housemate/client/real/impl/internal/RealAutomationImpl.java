package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Automation;
import com.intuso.housemate.client.api.internal.object.Condition;
import com.intuso.housemate.client.api.internal.object.Task;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealAutomation;
import com.intuso.housemate.client.real.api.internal.RealTask;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.housemate.client.real.impl.internal.utils.AddConditionCommand;
import com.intuso.housemate.client.real.impl.internal.utils.AddTaskCommand;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Base class for all automation
 */
public final class RealAutomationImpl
        extends RealObject<Automation.Data, Automation.Listener<? super RealAutomationImpl>>
        implements RealAutomation<RealCommandImpl, RealValueImpl<Boolean>, RealValueImpl<String>,
        RealListPersistedImpl<Condition.Data, RealConditionImpl>, RealListPersistedImpl<Task.Data, RealTaskImpl>, RealAutomationImpl>,
        Condition.Listener<RealConditionImpl> {

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<Boolean> runningValue;
    private final RealCommandImpl startCommand;
    private final RealCommandImpl stopCommand;
    private final RealValueImpl<String> errorValue;
    private final RealListPersistedImpl<Condition.Data, RealConditionImpl> conditions;
    private final RealCommandImpl addConditionCommand;
    private final RealListPersistedImpl<Task.Data, RealTaskImpl> satisfiedTasks;
    private final RealCommandImpl addSatisfiedTaskCommand;
    private final RealListPersistedImpl<Task.Data, RealTaskImpl> unsatisfiedTasks;
    private final RealCommandImpl addUnsatisfiedTaskCommand;

    private final RealListPersistedImpl.RemoveCallback<RealAutomationImpl> removeCallback;

    private final AddConditionCommand.Callback addConditionCallback = new AddConditionCommand.Callback() {

        @Override
        public void addCondition(RealConditionImpl condition) {
            conditions.add(condition);
        }
    };

    private final RealListPersistedImpl.RemoveCallback conditionRemoveCallback = new RealListPersistedImpl.RemoveCallback<RealConditionImpl>() {

        @Override
        public void remove(RealConditionImpl condition) {
            conditions.remove(condition.getId());
        }
    };

    private final AddTaskCommand.Callback addSatisfiedTaskCallback = new AddTaskCommand.Callback() {

        @Override
        public void addTask(RealTaskImpl task) {
            satisfiedTasks.add(task);
        }
    };

    private final RealListPersistedImpl.RemoveCallback satisfiedTaskRemoveCallback = new RealListPersistedImpl.RemoveCallback<RealTaskImpl>() {

        @Override
        public void remove(RealTaskImpl task) {
            satisfiedTasks.remove(task.getId());
        }
    };

    private final AddTaskCommand.Callback addUnsatisfiedTaskCallback = new AddTaskCommand.Callback() {

        @Override
        public void addTask(RealTaskImpl task) {
            unsatisfiedTasks.add(task);
        }
    };

    private final RealListPersistedImpl.RemoveCallback unsatisfiedTaskRemoveCallback = new RealListPersistedImpl.RemoveCallback<RealTaskImpl>() {

        @Override
        public void remove(RealTaskImpl task) {
            unsatisfiedTasks.remove(task.getId());
        }
    };

    private ManagedCollection.Registration conditionListenerRegistration;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealAutomationImpl(@Assisted final Logger logger,
                              @Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description,
                              @Assisted RealListPersistedImpl.RemoveCallback<RealAutomationImpl> removeCallback,
                              ManagedCollectionFactory managedCollectionFactory,
                              Sender.Factory senderFactory,
                              RealCommandImpl.Factory commandFactory,
                              RealParameterImpl.Factory parameterFactory,
                              RealValueImpl.Factory valueFactory,
                              RealListPersistedImpl.Factory<Condition.Data, RealConditionImpl> conditionsFactory,
                              RealListPersistedImpl.Factory<Task.Data, RealTaskImpl> tasksFactory,
                              AddConditionCommand.Factory addConditionCommandFactory,
                              AddTaskCommand.Factory addTaskCommandFactory,
                              TypeRepository typeRepository) {
        super(logger, new Automation.Data(id, name, description), managedCollectionFactory, senderFactory);
        this.removeCallback = removeCallback;
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the automation",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                            String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                            if (newName != null && !RealAutomationImpl.this.getName().equals(newName))
                                setName(newName);
                        }
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList(parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        Renameable.NAME_ID,
                        Renameable.NAME_ID,
                        "The new name",
                        typeRepository.getType(new TypeSpec(String.class)),
                        1,
                        1)));
        this.removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                Removeable.REMOVE_ID,
                Removeable.REMOVE_ID,
                "Remove the automation",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(isRunning())
                            throw new HousemateException("Cannot remove while automation is still running");
                        remove();
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.runningValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, Runnable.RUNNING_ID),
                Runnable.RUNNING_ID,
                Runnable.RUNNING_ID,
                "Whether the device is running or not",
                typeRepository.getType(new TypeSpec(Boolean.class)),
                1,
                1,
                Lists.newArrayList(false));
        this.startCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.START_ID),
                Runnable.START_ID,
                Runnable.START_ID,
                "Start the device",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if (!isRunning()) {
                            _start();
                            runningValue.setValue(true);
                        }
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.stopCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.STOP_ID),
                Runnable.STOP_ID,
                Runnable.STOP_ID,
                "Stop the device",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(isRunning()) {
                            _stop();
                            runningValue.setValue(false);
                        }
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.errorValue = (RealValueImpl<String>) valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID),
                Failable.ERROR_ID,
                Failable.ERROR_ID,
                "Current error for the automation",
                typeRepository.getType(new TypeSpec(String.class)),
                1,
                1,
                Lists.<String>newArrayList());
        this.conditions = conditionsFactory.create(ChildUtil.logger(logger, Automation.CONDITIONS_ID),
                Automation.CONDITIONS_ID,
                Automation.CONDITIONS_ID,
                Automation.CONDITIONS_ID);
        this.addConditionCommand = addConditionCommandFactory.create(ChildUtil.logger(logger, CONDITIONS_ID),
                ChildUtil.logger(logger, Automation.ADD_CONDITION_ID),
                Automation.ADD_CONDITION_ID,
                Automation.ADD_CONDITION_ID,
                "Add condition",
                addConditionCallback,
                conditionRemoveCallback);
        this.satisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, Automation.SATISFIED_TASKS_ID),
                Automation.SATISFIED_TASKS_ID,
                Automation.SATISFIED_TASKS_ID,
                Automation.SATISFIED_TASKS_ID);
        this.addSatisfiedTaskCommand = addTaskCommandFactory.create(ChildUtil.logger(logger, SATISFIED_TASKS_ID),
                ChildUtil.logger(logger, Automation.ADD_SATISFIED_TASK_ID),
                Automation.ADD_SATISFIED_TASK_ID,
                Automation.ADD_SATISFIED_TASK_ID,
                "Add satisfied task",
                addSatisfiedTaskCallback,
                satisfiedTaskRemoveCallback);
        this.unsatisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, Automation.UNSATISFIED_TASKS_ID),
                Automation.UNSATISFIED_TASKS_ID,
                Automation.UNSATISFIED_TASKS_ID,
                Automation.UNSATISFIED_TASKS_ID);
        this.addUnsatisfiedTaskCommand = addTaskCommandFactory.create(ChildUtil.logger(logger, UNSATISFIED_TASKS_ID),
                ChildUtil.logger(logger, Automation.ADD_UNSATISFIED_TASK_ID),
                Automation.ADD_UNSATISFIED_TASK_ID,
                Automation.ADD_UNSATISFIED_TASK_ID,
                "Add unsatisfied task",
                addUnsatisfiedTaskCallback,
                unsatisfiedTaskRemoveCallback);
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID));
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID));
        runningValue.init(ChildUtil.name(name, Runnable.RUNNING_ID));
        startCommand.init(ChildUtil.name(name, Runnable.START_ID));
        stopCommand.init(ChildUtil.name(name, Runnable.STOP_ID));
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID));
        conditions.init(ChildUtil.name(name, Automation.CONDITIONS_ID));
        addConditionCommand.init(ChildUtil.name(name, Automation.ADD_CONDITION_ID));
        satisfiedTasks.init(ChildUtil.name(name, Automation.SATISFIED_TASKS_ID));
        addSatisfiedTaskCommand.init(ChildUtil.name(name, Automation.ADD_SATISFIED_TASK_ID));
        unsatisfiedTasks.init(ChildUtil.name(name, Automation.UNSATISFIED_TASKS_ID));
        addUnsatisfiedTaskCommand.init(ChildUtil.name(name, Automation.ADD_UNSATISFIED_TASK_ID));
        if(isRunning())
            _start();
    }

    @Override
    protected void uninitChildren() {
        _stop();
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        runningValue.uninit();
        startCommand.uninit();
        stopCommand.uninit();
        errorValue.uninit();
        conditions.uninit();
        addConditionCommand.uninit();
        satisfiedTasks.uninit();
        addSatisfiedTaskCommand.uninit();
        unsatisfiedTasks.uninit();
        addUnsatisfiedTaskCommand.uninit();
    }

    private void setName(String newName) {
        RealAutomationImpl.this.getData().setName(newName);
        for(Automation.Listener<? super RealAutomationImpl> listener : listeners)
            listener.renamed(RealAutomationImpl.this, RealAutomationImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    @Override
    public RealCommandImpl getRenameCommand() {
        return renameCommand;
    }

    @Override
    public RealCommandImpl getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealValueImpl<Boolean> getRunningValue() {
        return runningValue;
    }

    @Override
    public boolean isRunning() {
        return runningValue.getValue() != null ? runningValue.getValue() : false;
    }

    @Override
    public RealCommandImpl getStartCommand() {
        return startCommand;
    }

    @Override
    public RealCommandImpl getStopCommand() {
        return stopCommand;
    }

    @Override
    public RealValueImpl<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public RealListPersistedImpl<Condition.Data, RealConditionImpl> getConditions() {
        return conditions;
    }

    @Override
    public RealCommandImpl getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public RealListPersistedImpl<Task.Data, RealTaskImpl> getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public RealCommandImpl getAddSatisfiedTaskCommand() {
        return addSatisfiedTaskCommand;
    }

    @Override
    public RealListPersistedImpl<Task.Data, RealTaskImpl> getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }

    @Override
    public RealCommandImpl getAddUnsatisfiedTaskCommand() {
        return addUnsatisfiedTaskCommand;
    }

    @Override
    public void renamed(RealConditionImpl condition, String oldName, String newName) {
        // do nothing for now
    }

    @Override
    public void driverLoaded(RealConditionImpl condition, boolean loaded) {
        // do nothing for now
    }

    @Override
    public void error(RealConditionImpl condition, String error) {
        // do nothing for now
    }

    @Override
    public void conditionSatisfied(RealConditionImpl condition, boolean satisfied) {
        try {
            logger.debug("Automation " + (satisfied ? "" : "un") + "satisfied, executing tasks");
            for(RealTask task : (satisfied ? satisfiedTasks : unsatisfiedTasks))
                task.executeTask();
        } catch (Throwable t) {
            getErrorValue().setValue("Failed to perform automation tasks: " + t.getMessage());
            logger.error("Failed to perform automation tasks", t);
        }
    }

    protected final void remove() {
        removeCallback.remove(this);
    }

    private void _start() {
        try {
            if(conditions.size() == 0)
                throw new HousemateException("Automation has no condition. It must have exactly one");
            else if(conditions.size() > 1)
                throw new HousemateException(("Automation has multiple conditions. It can only have one"));
            else {
                RealConditionImpl condition = conditions.iterator().next();
                condition.start();
                conditionListenerRegistration = condition.addObjectListener(this);
                conditionSatisfied(condition, condition.isSatisfied());
            }
        } catch (Throwable t) {
            getErrorValue().setValue("Could not start automation: " + t.getMessage());
        }
    }

    private void _stop() {
        for(RealConditionImpl condition : conditions)
            condition.stop();
        if(conditionListenerRegistration != null) {
            conditionListenerRegistration.remove();
            conditionListenerRegistration = null;
        }
    }

    public interface Factory {
        RealAutomationImpl create(Logger logger,
                                  @Assisted("id") String id,
                                  @Assisted("name") String name,
                                  @Assisted("description") String description,
                                  RealListPersistedImpl.RemoveCallback<RealAutomationImpl> removeCallback);
    }

    public static class LoadPersisted implements RealListPersistedImpl.ElementFactory<Automation.Data, RealAutomationImpl> {

        private final RealAutomationImpl.Factory factory;

        @Inject
        public LoadPersisted(Factory factory) {
            this.factory = factory;
        }

        @Override
        public RealAutomationImpl create(Logger logger, Automation.Data data, RealListPersistedImpl.RemoveCallback<RealAutomationImpl> removeCallback) {
            return factory.create(logger, data.getId(), data.getName(), data.getDescription(), removeCallback);
        }
    }
}

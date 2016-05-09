package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.real.api.internal.RealAutomation;
import com.intuso.housemate.client.real.api.internal.RealCondition;
import com.intuso.housemate.client.real.api.internal.RealTask;
import com.intuso.housemate.client.real.impl.internal.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.real.impl.internal.factory.task.AddTaskCommand;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * Base class for all automation
 */
public final class RealAutomationImpl
        extends RealObject<Automation.Data, Automation.Listener<? super RealAutomationImpl>>
        implements RealAutomation<RealCommandImpl, RealValueImpl<Boolean>, RealValueImpl<String>,
                RealListImpl<RealConditionImpl<?>>, RealListImpl<RealTaskImpl<?>>, RealAutomationImpl>,
        Condition.Listener<RealConditionImpl<?>> {

    private final static String PROPERTIES_DESCRIPTION = "The automation's properties";

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<Boolean> runningValue;
    private final RealCommandImpl startCommand;
    private final RealCommandImpl stopCommand;
    private final RealValueImpl<String> errorValue;
    private final RealListImpl<RealConditionImpl<?>> conditions;
    private final RealCommandImpl addConditionCommand;
    private final RealListImpl<RealTaskImpl<?>> satisfiedTasks;
    private final RealCommandImpl addSatisfiedTaskCommand;
    private final RealListImpl<RealTaskImpl<?>> unsatisfiedTasks;
    private final RealCommandImpl addUnsatisfiedTaskCommand;

    private final RemoveCallback<RealAutomationImpl> removeCallback;

    private final AddConditionCommand.Callback addConditionCallback = new AddConditionCommand.Callback() {

        @Override
        public void addCondition(RealConditionImpl<?> condition) {
            conditions.add(condition);
        }
    };

    private final RealCondition.RemoveCallback conditionRemoveCallback = new RealCondition.RemoveCallback<RealConditionImpl<?>>() {

        @Override
        public void removeCondition(RealConditionImpl<?> condition) {
            conditions.remove(condition.getId());
        }
    };

    private final AddTaskCommand.Callback addSatisfiedTaskCallback = new AddTaskCommand.Callback() {

        @Override
        public void addTask(RealTaskImpl<?> task) {
            satisfiedTasks.add(task);
        }
    };

    private final RealTask.RemoveCallback satisfiedTaskRemoveCallback = new RealTask.RemoveCallback<RealTaskImpl<?>>() {

        @Override
        public void removeTask(RealTaskImpl<?> task) {
            satisfiedTasks.remove(task.getId());
        }
    };

    private final AddTaskCommand.Callback addUnsatisfiedTaskCallback = new AddTaskCommand.Callback() {

        @Override
        public void addTask(RealTaskImpl<?> task) {
            unsatisfiedTasks.add(task);
        }
    };

    private final RealTask.RemoveCallback unsatisfiedTaskRemoveCallback = new RealTask.RemoveCallback<RealTaskImpl<?>>() {

        @Override
        public void removeTask(RealTaskImpl<?> task) {
            unsatisfiedTasks.remove(task.getId());
        }
    };

    private ListenerRegistration conditionListenerRegistration;

    /**
     * @param logger {@inheritDoc}
     * @param data the automation's data
     * @param listenersFactory
     */
    @Inject
    public RealAutomationImpl(@Assisted Logger logger,
                              @Assisted Automation.Data data,
                              ListenersFactory listenersFactory,
                              @Assisted RemoveCallback<RealAutomationImpl> removeCallback,
                              AddConditionCommand.Factory addConditionCommandFactory,
                              AddTaskCommand.Factory addTaskCommandFactory) {
        super(logger, data, listenersFactory);
        this.removeCallback = removeCallback;
        this.renameCommand = new RealCommandImpl(ChildUtil.logger(logger, Renameable.RENAME_ID),
                new Command.Data(Renameable.RENAME_ID, Renameable.RENAME_ID, "Rename the automation"),
                listenersFactory,
                StringType.createParameter(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        new Parameter.Data(Renameable.NAME_ID, Renameable.NAME_ID, "The new name"),
                        listenersFactory)) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                    String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                    if (newName != null && !RealAutomationImpl.this.getName().equals(newName))
                        setName(newName);
                }
            }
        };
        this.removeCommand = new RealCommandImpl(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                new Command.Data(Removeable.REMOVE_ID, Removeable.REMOVE_ID, "Remove the automation"),
                listenersFactory) {
            @Override
            public void perform(Type.InstanceMap values) {
                remove();
            }
        };
        this.runningValue = BooleanType.createValue(ChildUtil.logger(logger, Runnable.RUNNING_ID),
                new Value.Data(Runnable.RUNNING_ID, Runnable.RUNNING_ID, "Whether the device is running or not"),
                listenersFactory,
                false);
        this.startCommand = new RealCommandImpl(ChildUtil.logger(logger, Runnable.START_ID),
                new Command.Data(Runnable.START_ID, Runnable.START_ID, "Start the device"),
                listenersFactory) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(!isRunning()) {
                    _start();
                    runningValue.setValue(true);
                }
            }
        };
        this.stopCommand = new RealCommandImpl(ChildUtil.logger(logger, Runnable.STOP_ID),
                new Command.Data(Runnable.STOP_ID, Runnable.STOP_ID, "Stop the device"),
                listenersFactory) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(isRunning()) {
                    _stop();
                    runningValue.setValue(false);
                }
            }
        };
        this.errorValue = StringType.createValue(ChildUtil.logger(logger, Failable.ERROR_ID),
                new Value.Data(Failable.ERROR_ID, Failable.ERROR_ID, "Current error for the automation"),
                listenersFactory,
                null);
        this.conditions = new RealListImpl<>(ChildUtil.logger(logger, Automation.CONDITIONS_ID), new List.Data(Automation.CONDITIONS_ID, Automation.CONDITIONS_ID, Automation.CONDITIONS_ID), listenersFactory);
        this.addConditionCommand = addConditionCommandFactory.create(ChildUtil.logger(logger, Automation.ADD_CONDITION_ID),
                new Command.Data(Automation.ADD_CONDITION_ID, Automation.ADD_CONDITION_ID, "Add condition"),
                addConditionCallback,
                conditionRemoveCallback);
        this.satisfiedTasks = new RealListImpl<>(ChildUtil.logger(logger, Automation.SATISFIED_TASKS_ID), new List.Data(Automation.SATISFIED_TASKS_ID, Automation.SATISFIED_TASKS_ID, Automation.SATISFIED_TASKS_ID), listenersFactory);
        this.addSatisfiedTaskCommand = addTaskCommandFactory.create(ChildUtil.logger(logger, Automation.ADD_SATISFIED_TASK_ID),
                new Command.Data(Automation.ADD_SATISFIED_TASK_ID, Automation.ADD_SATISFIED_TASK_ID, "Add satisfied task"),
                addSatisfiedTaskCallback,
                satisfiedTaskRemoveCallback);
        this.unsatisfiedTasks = new RealListImpl<>(ChildUtil.logger(logger, Automation.UNSATISFIED_TASKS_ID), new List.Data(Automation.UNSATISFIED_TASKS_ID, Automation.UNSATISFIED_TASKS_ID, Automation.UNSATISFIED_TASKS_ID), listenersFactory);
        this.addUnsatisfiedTaskCommand = addTaskCommandFactory.create(ChildUtil.logger(logger, Automation.ADD_UNSATISFIED_TASK_ID),
                new Command.Data(Automation.ADD_UNSATISFIED_TASK_ID, Automation.ADD_UNSATISFIED_TASK_ID, "Add unsatisfied task"),
                addUnsatisfiedTaskCallback,
                unsatisfiedTaskRemoveCallback);
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), session);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), session);
        runningValue.init(ChildUtil.name(name, Runnable.RUNNING_ID), session);
        startCommand.init(ChildUtil.name(name, Runnable.START_ID), session);
        stopCommand.init(ChildUtil.name(name, Runnable.STOP_ID), session);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), session);
        conditions.init(ChildUtil.name(name, Automation.CONDITIONS_ID), session);
        satisfiedTasks.init(ChildUtil.name(name, Automation.SATISFIED_TASKS_ID), session);
        unsatisfiedTasks.init(ChildUtil.name(name, Automation.UNSATISFIED_TASKS_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        runningValue.uninit();
        startCommand.uninit();
        stopCommand.uninit();
        errorValue.uninit();
        conditions.uninit();
        satisfiedTasks.uninit();
        unsatisfiedTasks.uninit();
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
    public RealListImpl<RealConditionImpl<?>> getConditions() {
        return conditions;
    }

    @Override
    public RealCommandImpl getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public RealListImpl<RealTaskImpl<?>> getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public RealCommandImpl getAddSatisfiedTaskCommand() {
        return addSatisfiedTaskCommand;
    }

    @Override
    public RealListImpl<RealTaskImpl<?>> getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }

    @Override
    public RealCommandImpl getAddUnsatisfiedTaskCommand() {
        return addUnsatisfiedTaskCommand;
    }

    @Override
    public void renamed(RealConditionImpl<?> condition, String oldName, String newName) {
        // do nothing for now
    }

    @Override
    public void driverLoaded(RealConditionImpl<?> condition, boolean loaded) {
        // do nothing for now
    }

    @Override
    public void error(RealConditionImpl<?> condition, String error) {
        // do nothing for now
    }

    @Override
    public void conditionSatisfied(RealConditionImpl<?> condition, boolean satisfied) {
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
        removeCallback.removeAutomation(this);
    }

    private void _start() {
        try {
            if(conditions.size() == 0)
                throw new HousemateException("Automation has no condition. It must have exactly one");
            else if(conditions.size() > 1)
                throw new HousemateException(("Automation has multiple conditions. It can only have one"));
            else {
                RealConditionImpl<?> condition = conditions.iterator().next();
                condition.start();
                conditionListenerRegistration = condition.addObjectListener(this);
                conditionSatisfied(condition, condition.isSatisfied());
            }
        } catch (Throwable t) {
            getErrorValue().setValue("Could not start automation: " + t.getMessage());
        }
    }

    private void _stop() {
        for(RealConditionImpl<?> condition : conditions)
            condition.stop();
        if(conditionListenerRegistration != null) {
            conditionListenerRegistration.removeListener();
            conditionListenerRegistration = null;
        }
    }

    public interface Factory {
        RealAutomationImpl create(Logger logger, Automation.Data data, RemoveCallback<RealAutomationImpl> removeCallback);
    }
}

package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Automation;
import com.intuso.housemate.client.proxy.api.internal.*;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * @param <COMMAND> the type of the add command
 * @param <VALUE> the type of the value
 * @param <CONDITIONS> the type of the conditions list
 * @param <TASKS> the type of the tasks list
 * @param <AUTOMATION> the type of the automation
 */
public abstract class ProxyAutomation<
            COMMAND extends ProxyCommand<?, ?, COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            CONDITIONS extends ProxyList<? extends ProxyCondition<?, ?, ?, ?, ?, ?>, ?>,
            TASKS extends ProxyList<? extends ProxyTask<?, ?, ?, ?, ?>, ?>,
            AUTOMATION extends ProxyAutomation<COMMAND, VALUE, CONDITIONS, TASKS, AUTOMATION>>
        extends ProxyObject<Automation.Data, Automation.Listener<? super AUTOMATION>>
        implements Automation<COMMAND, COMMAND, VALUE, COMMAND, VALUE, COMMAND, CONDITIONS, TASKS, AUTOMATION>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND>,
        ProxyRenameable<COMMAND>,
        ProxyRunnable<COMMAND, VALUE> {

    private final COMMAND renameCommand;
    private final COMMAND removeCommand;
    private final VALUE runningValue;
    private final COMMAND startCommand;
    private final COMMAND stopCommand;
    private final VALUE errorValue;
    private final CONDITIONS conditions;
    private final COMMAND addConditionCommand;
    private final TASKS satisfiedTasks;
    private final COMMAND addSatisfiedTaskCommand;
    private final TASKS unsatisfiedTasks;
    private final COMMAND addUnsatisfiedTaskCommand;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyAutomation(Logger logger,
                           ListenersFactory listenersFactory,
                           ProxyObject.Factory<COMMAND> commandFactory,
                           ProxyObject.Factory<VALUE> valueFactory,
                           ProxyObject.Factory<CONDITIONS> conditionsFactory,
                           ProxyObject.Factory<TASKS> tasksFactory) {
        super(logger, Automation.Data.class, listenersFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        runningValue = valueFactory.create(ChildUtil.logger(logger, Runnable.RUNNING_ID));
        startCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.START_ID));
        stopCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.STOP_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        conditions = conditionsFactory.create(ChildUtil.logger(logger, Automation.CONDITIONS_ID));
        addConditionCommand = commandFactory.create(ChildUtil.logger(logger, Automation.ADD_CONDITION_ID));
        satisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, Automation.SATISFIED_TASKS_ID));
        addSatisfiedTaskCommand = commandFactory.create(ChildUtil.logger(logger, Automation.ADD_SATISFIED_TASK_ID));
        unsatisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, Automation.UNSATISFIED_TASKS_ID));
        addUnsatisfiedTaskCommand = commandFactory.create(ChildUtil.logger(logger, Automation.ADD_UNSATISFIED_TASK_ID));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), connection);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), connection);
        runningValue.init(ChildUtil.name(name, Runnable.RUNNING_ID), connection);
        startCommand.init(ChildUtil.name(name, Runnable.START_ID), connection);
        stopCommand.init(ChildUtil.name(name, Runnable.STOP_ID), connection);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), connection);
        conditions.init(ChildUtil.name(name, Automation.CONDITIONS_ID), connection);
        addConditionCommand.init(ChildUtil.name(name, Automation.ADD_CONDITION_ID), connection);
        satisfiedTasks.init(ChildUtil.name(name, Automation.SATISFIED_TASKS_ID), connection);
        addSatisfiedTaskCommand.init(ChildUtil.name(name, Automation.ADD_SATISFIED_TASK_ID), connection);
        unsatisfiedTasks.init(ChildUtil.name(name, Automation.UNSATISFIED_TASKS_ID), connection);
        addUnsatisfiedTaskCommand.init(ChildUtil.name(name, Automation.ADD_UNSATISFIED_TASK_ID), connection);
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
        addConditionCommand.uninit();
        satisfiedTasks.uninit();
        addSatisfiedTaskCommand.uninit();
        unsatisfiedTasks.uninit();
        addUnsatisfiedTaskCommand.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public final boolean isRunning() {
        return runningValue.getValue() != null
                && runningValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(runningValue.getValue().getFirstValue());
    }

    @Override
    public VALUE getRunningValue() {
        return runningValue;
    }

    @Override
    public COMMAND getStartCommand() {
        return startCommand;
    }

    @Override
    public COMMAND getStopCommand() {
        return stopCommand;
    }

    @Override
    public final String getError() {
        return errorValue.getValue() != null ? errorValue.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return errorValue;
    }

    @Override
    public CONDITIONS getConditions() {
        return conditions;
    }

    @Override
    public COMMAND getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public TASKS getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public COMMAND getAddSatisfiedTaskCommand() {
        return addSatisfiedTaskCommand;
    }

    @Override
    public TASKS getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }

    @Override
    public COMMAND getAddUnsatisfiedTaskCommand() {
        return addUnsatisfiedTaskCommand;
    }
}

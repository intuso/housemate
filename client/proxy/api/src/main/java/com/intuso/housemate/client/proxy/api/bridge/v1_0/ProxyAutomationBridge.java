package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.AutomationMapper;
import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Automation;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyAutomationBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Automation.Data, Automation.Data, Automation.Listener<? super ProxyAutomationBridge>>
        implements Automation<ProxyCommandBridge, ProxyCommandBridge, ProxyValueBridge, ProxyCommandBridge, ProxyValueBridge, ProxyCommandBridge, ProxyListBridge<ProxyConditionBridge>, ProxyListBridge<ProxyTaskBridge>, ProxyAutomationBridge> {

    private final ProxyCommandBridge renameCommand;
    private final ProxyCommandBridge removeCommand;
    private final ProxyValueBridge runningValue;
    private final ProxyCommandBridge startCommand;
    private final ProxyCommandBridge stopCommand;
    private final ProxyValueBridge errorValue;
    private final ProxyCommandBridge addConditionCommand;
    private final ProxyListBridge<ProxyConditionBridge> conditions;
    private final ProxyCommandBridge addSatisfiedTaskCommand;
    private final ProxyListBridge<ProxyTaskBridge> satisfiedTasks;
    private final ProxyCommandBridge addUnsatisfiedTaskCommand;
    private final ProxyListBridge<ProxyTaskBridge> unsatisfiedTasks;

    @Inject
    protected ProxyAutomationBridge(@Assisted Logger logger,
                                    AutomationMapper automationMapper,
                                    Factory<ProxyCommandBridge> commandFactory,
                                    Factory<ProxyValueBridge> valueFactory,
                                    Factory<ProxyListBridge<ProxyConditionBridge>> conditionsFactory,
                                    Factory<ProxyListBridge<ProxyTaskBridge>> tasksFactory,
                                    ManagedCollectionFactory managedCollectionFactory) {
        super(logger, Automation.Data.class, automationMapper, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        runningValue = valueFactory.create(ChildUtil.logger(logger, Runnable.RUNNING_ID));
        startCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.START_ID));
        stopCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.STOP_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        addConditionCommand = commandFactory.create(ChildUtil.logger(logger, Automation.ADD_CONDITION_ID));
        conditions = conditionsFactory.create(ChildUtil.logger(logger, Automation.CONDITIONS_ID));
        addSatisfiedTaskCommand = commandFactory.create(ChildUtil.logger(logger, Automation.ADD_SATISFIED_TASK_ID));
        satisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, Automation.SATISFIED_TASKS_ID));
        addUnsatisfiedTaskCommand = commandFactory.create(ChildUtil.logger(logger, Automation.ADD_UNSATISFIED_TASK_ID));
        unsatisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, Automation.UNSATISFIED_TASKS_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        renameCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID),
                connection);
        removeCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Removeable.REMOVE_ID),
                ChildUtil.name(internalName, Removeable.REMOVE_ID),
                connection);
        runningValue.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.RUNNING_ID),
                ChildUtil.name(internalName, Runnable.RUNNING_ID),
                connection);
        startCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.START_ID),
                ChildUtil.name(internalName, Runnable.START_ID),
                connection);
        stopCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.STOP_ID),
                ChildUtil.name(internalName, Runnable.STOP_ID),
                connection);
        errorValue.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Failable.ERROR_ID),
                ChildUtil.name(internalName, Failable.ERROR_ID),
                connection);
        addConditionCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Automation.ADD_CONDITION_ID),
                ChildUtil.name(internalName, Automation.ADD_CONDITION_ID),
                connection);
        conditions.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Automation.CONDITIONS_ID),
                ChildUtil.name(internalName, Automation.CONDITIONS_ID),
                connection);
        addSatisfiedTaskCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Automation.ADD_SATISFIED_TASK_ID),
                ChildUtil.name(internalName, Automation.ADD_SATISFIED_TASK_ID),
                connection);
        satisfiedTasks.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Automation.SATISFIED_TASKS_ID),
                ChildUtil.name(internalName, Automation.SATISFIED_TASKS_ID),
                connection);
        addUnsatisfiedTaskCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Automation.ADD_UNSATISFIED_TASK_ID),
                ChildUtil.name(internalName, Automation.ADD_UNSATISFIED_TASK_ID),
                connection);
        unsatisfiedTasks.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Automation.UNSATISFIED_TASKS_ID),
                ChildUtil.name(internalName, Automation.UNSATISFIED_TASKS_ID),
                connection);
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
        addConditionCommand.uninit();
        conditions.uninit();
        addSatisfiedTaskCommand.uninit();
        satisfiedTasks.uninit();
        addUnsatisfiedTaskCommand.uninit();
        unsatisfiedTasks.uninit();
    }

    @Override
    public ProxyCommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public ProxyCommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ProxyValueBridge getRunningValue() {
        return runningValue;
    }

    @Override
    public ProxyCommandBridge getStartCommand() {
        return startCommand;
    }

    @Override
    public ProxyCommandBridge getStopCommand() {
        return stopCommand;
    }

    @Override
    public ProxyValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public ProxyCommandBridge getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public ProxyListBridge<ProxyConditionBridge> getConditions() {
        return conditions;
    }

    @Override
    public ProxyCommandBridge getAddSatisfiedTaskCommand() {
        return addSatisfiedTaskCommand;
    }

    @Override
    public ProxyListBridge<ProxyTaskBridge> getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public ProxyCommandBridge getAddUnsatisfiedTaskCommand() {
        return addUnsatisfiedTaskCommand;
    }

    @Override
    public ProxyListBridge<ProxyTaskBridge> getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }
}

package com.intuso.housemate.object.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.AutomationListener;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerProxyAutomation
        extends ServerProxyPrimaryObject<
            AutomationData,
            ServerProxyAutomation,
            AutomationListener<? super ServerProxyAutomation>>
        implements Automation<
            ServerProxyCommand,
            ServerProxyCommand,
            ServerProxyCommand,
            ServerProxyCommand,
            ServerProxyValue,
            ServerProxyValue,
            ServerProxyCondition,
            ServerProxyList<ConditionData, ServerProxyCondition>,
            ServerProxyTask,
            ServerProxyList<TaskData, ServerProxyTask>,
            ServerProxyAutomation> {

    private ServerProxyList<ConditionData, ServerProxyCondition> conditions;
    private ServerProxyCommand addCondition;
    private ServerProxyList<TaskData, ServerProxyTask> satisfiedTasks;
    private ServerProxyCommand addSatisifedTask;
    private ServerProxyList<TaskData, ServerProxyTask> unsatisfiedTasks;
    private ServerProxyCommand addUnsatisfiedTask;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyAutomation(Log log, ListenersFactory listenersFactory, Injector injector, BooleanType booleanType, @Assisted AutomationData data) {
        super(log, listenersFactory, injector, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        conditions = (ServerProxyList<ConditionData, ServerProxyCondition>) getChild(CONDITIONS_ID);
        addCondition = (ServerProxyCommand) getChild(ADD_CONDITION_ID);
        satisfiedTasks = (ServerProxyList<TaskData, ServerProxyTask>) getChild(SATISFIED_TASKS_ID);
        addSatisifedTask = (ServerProxyCommand) getChild(ADD_SATISFIED_TASK_ID);
        unsatisfiedTasks = (ServerProxyList<TaskData, ServerProxyTask>) getChild(UNSATISFIED_TASKS_ID);
        addUnsatisfiedTask = (ServerProxyCommand) getChild(ADD_UNSATISFIED_TASK_ID);
    }

    @Override
    public ServerProxyList<TaskData, ServerProxyTask> getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public ServerProxyList<TaskData, ServerProxyTask> getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }

    @Override
    public ServerProxyCommand getAddConditionCommand() {
        return addCondition;
    }

    @Override
    public ServerProxyCommand getAddSatisifedTaskCommand() {
        return addSatisifedTask;
    }

    @Override
    public ServerProxyCommand getAddUnsatisifedTaskCommand() {
        return addUnsatisfiedTask;
    }

    @Override
    public ServerProxyList<ConditionData, ServerProxyCondition> getConditions() {
        return conditions;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof AutomationData) {
            getConditions().copyValues(data.getChildData(CONDITIONS_ID));
            getSatisfiedTasks().copyValues(data.getChildData(SATISFIED_TASKS_ID));
            getUnsatisfiedTasks().copyValues(data.getChildData(UNSATISFIED_TASKS_ID));
        }
    }
}

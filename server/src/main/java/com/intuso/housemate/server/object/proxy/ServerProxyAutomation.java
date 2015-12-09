package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.Automation;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.object.ObjectFactory;
import org.slf4j.Logger;

import java.util.List;

public class ServerProxyAutomation
        extends ServerProxyObject<AutomationData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyAutomation, Automation.Listener<? super ServerProxyAutomation>>
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

    private ServerProxyCommand rename;
    private ServerProxyCommand remove;
    private ServerProxyValue running;
    private ServerProxyCommand start;
    private ServerProxyCommand stop;
    private ServerProxyValue error;
    private ServerProxyList<ConditionData, ServerProxyCondition> conditions;
    private ServerProxyCommand addCondition;
    private ServerProxyList<TaskData, ServerProxyTask> satisfiedTasks;
    private ServerProxyCommand addSatisifedTask;
    private ServerProxyList<TaskData, ServerProxyTask> unsatisfiedTasks;
    private ServerProxyCommand addUnsatisfiedTask;

    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyAutomation(Logger logger, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted AutomationData data) {
        super(logger, listenersFactory, objectFactory, data);
    }

    @Override
    protected void getChildObjects() {
        rename = (ServerProxyCommand) getChild(AutomationData.RENAME_ID);
        remove = (ServerProxyCommand) getChild(AutomationData.REMOVE_ID);
        running = (ServerProxyValue) getChild(AutomationData.RUNNING_ID);
        start = (ServerProxyCommand) getChild(AutomationData.START_ID);
        stop = (ServerProxyCommand) getChild(AutomationData.STOP_ID);
        error = (ServerProxyValue) getChild(AutomationData.ERROR_ID);
        conditions = (ServerProxyList<ConditionData, ServerProxyCondition>) getChild(AutomationData.CONDITIONS_ID);
        addCondition = (ServerProxyCommand) getChild(AutomationData.ADD_CONDITION_ID);
        satisfiedTasks = (ServerProxyList<TaskData, ServerProxyTask>) getChild(AutomationData.SATISFIED_TASKS_ID);
        addSatisifedTask = (ServerProxyCommand) getChild(AutomationData.ADD_SATISFIED_TASK_ID);
        unsatisfiedTasks = (ServerProxyList<TaskData, ServerProxyTask>) getChild(AutomationData.UNSATISFIED_TASKS_ID);
        addUnsatisfiedTask = (ServerProxyCommand) getChild(AutomationData.ADD_UNSATISFIED_TASK_ID);
    }

    @Override
    public ServerProxyCommand getRenameCommand() {
        return rename;
    }

    @Override
    public ServerProxyCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public ServerProxyValue getRunningValue() {
        return running;
    }

    @Override
    public ServerProxyCommand getStartCommand() {
        return start;
    }

    @Override
    public ServerProxyCommand getStopCommand() {
        return stop;
    }

    @Override
    public ServerProxyValue getErrorValue() {
        return error;
    }

    @Override
    public List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(AutomationData.NEW_NAME, new Message.Receiver<ClientPayload<StringPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<StringPayload>> message) {
                String oldName = getData().getName();
                String newName = message.getPayload().getOriginal().getValue();
                getData().setName(newName);
                for(Automation.Listener<? super ServerProxyAutomation> listener : getObjectListeners())
                    listener.renamed(getThis(), oldName, newName);
            }
        }));
        return result;
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
            getConditions().copyValues(data.getChildData(AutomationData.CONDITIONS_ID));
            getSatisfiedTasks().copyValues(data.getChildData(AutomationData.SATISFIED_TASKS_ID));
            getUnsatisfiedTasks().copyValues(data.getChildData(AutomationData.UNSATISFIED_TASKS_ID));
        }
    }
}

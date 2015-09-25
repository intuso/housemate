package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.real.api.internal.factory.task.RealTaskOwner;
import com.intuso.housemate.client.real.api.internal.impl.type.BooleanType;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.comms.api.internal.payload.TaskData;
import com.intuso.housemate.object.api.internal.Task;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public abstract class RealTask
        extends RealObject<
        TaskData,
        HousemateData<?>,
            RealObject<?, ?, ?, ?>,
            Task.Listener<? super RealTask>>
        implements Task<
            RealCommand,
            RealValue<Boolean>,
            RealValue<String>,
            RealList<PropertyData, RealProperty<?>>, RealTask> {

    private final String type;

    private RealCommand removeCommand;
    private RealValue<String> errorValue;
    private RealValue<Boolean> executingValue;
    private RealList<PropertyData, RealProperty<?>> propertyList;

    /**
     * @param log {@inheritDoc}
     * @param data the task's data
     */
    public RealTask(Log log, ListenersFactory listenersFactory, String type, TaskData data,
                    RealTaskOwner owner, RealProperty<?>... properties) {
        this(log, listenersFactory, type, data, owner, Lists.newArrayList(properties));
    }

    /**
     * @param log {@inheritDoc}
     * @param data the object's data
     * @param properties the task's properties
     */
    public RealTask(Log log, ListenersFactory listenersFactory, String type, TaskData data,
                    final RealTaskOwner owner, List<RealProperty<?>> properties) {
        super(log, listenersFactory, data);
        this.type = type;
        removeCommand = new RealCommand(log, listenersFactory, TaskData.REMOVE_ID, TaskData.REMOVE_ID, "Remove the task", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                owner.removeTask(RealTask.this);
            }
        };
        errorValue = new RealValue<>(log, listenersFactory, TaskData.ERROR_ID, TaskData.ERROR_ID, "The current error", new StringType(log, listenersFactory), (List)null);
        executingValue = new RealValue<>(log, listenersFactory, TaskData.EXECUTING_ID, TaskData.EXECUTING_ID, "Whether the task is executing", new BooleanType(log, listenersFactory), false);
        propertyList = new RealList<>(log, listenersFactory, TaskData.PROPERTIES_ID, TaskData.PROPERTIES_ID, "The task's properties", properties);
        addChild(removeCommand);
        addChild(errorValue);
        addChild(executingValue);
        addChild(propertyList);
    }

    public String getType() {
        return type;
    }

    @Override
    public RealCommand getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealList<PropertyData, RealProperty<?>> getProperties() {
        return propertyList;
    }

    @Override
    public RealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public RealValue<Boolean> getExecutingValue() {
        return executingValue;
    }

    public boolean isExecuting() {
        return executingValue.getTypedValue() != null ? executingValue.getTypedValue() : false;
    }

    /**
     * Sets the error message for this task
     * @param error the error message for this task
     */
    public final void setError(String error) {
        getErrorValue().setTypedValues(error);
    }

    /**
     * Sets the executing value for this task
     * @param executing the executing value for this task
     */
    private void taskExecuting(boolean executing) {
        if(executing != isExecuting()) {
            getExecutingValue().setTypedValues(executing);
            for(Task.Listener<? super RealTask> listener : getObjectListeners())
                listener.taskExecuting(this, executing);
        }
    }

    /**
     * Executes this task
     */
    public final void executeTask() {
        getLog().d("Performing task " + getId());
        taskExecuting(true);
        execute();
        taskExecuting(false);
    }

    /**
     * Does the actual task execution
     */
    protected abstract void execute();
}

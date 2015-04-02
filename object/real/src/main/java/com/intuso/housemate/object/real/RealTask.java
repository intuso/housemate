package com.intuso.housemate.object.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.task.TaskListener;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.factory.task.RealTaskOwner;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public abstract class RealTask
        extends RealObject<
            TaskData,
            HousemateData<?>,
            RealObject<?, ?, ?, ?>,
            TaskListener<? super RealTask>>
        implements Task<
            RealCommand,
            RealValue<Boolean>,
            RealValue<String>,
            RealList<PropertyData, RealProperty<?>>, RealTask> {

    private RealCommand removeCommand;
    private RealValue<String> errorValue;
    private RealValue<Boolean> executingValue;
    private RealList<PropertyData, RealProperty<?>> propertyList;

    /**
     * @param log {@inheritDoc}
     * @param data the task's data
     */
    public RealTask(Log log, ListenersFactory listenersFactory, TaskData data,
                    RealTaskOwner owner, RealProperty<?>... properties) {
        this(log, listenersFactory, data, owner, Lists.newArrayList(properties));
    }

    /**
     * @param log {@inheritDoc}
     * @param data the object's data
     * @param properties the task's properties
     */
    public RealTask(Log log, ListenersFactory listenersFactory, TaskData data,
                    final RealTaskOwner owner, java.util.List<RealProperty<?>> properties) {
        super(log, listenersFactory, data);
        removeCommand = new RealCommand(log, listenersFactory, REMOVE_ID, REMOVE_ID, "Remove the task", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                owner.removeTask(RealTask.this);
            }
        };
        errorValue = new RealValue<>(log, listenersFactory, ERROR_ID, ERROR_ID, "The current error", new StringType(log, listenersFactory), (List)null);
        executingValue = new RealValue<>(log, listenersFactory, EXECUTING_ID, EXECUTING_ID, "Whether the task is executing", new BooleanType(log, listenersFactory), false);
        propertyList = new RealList<>(log, listenersFactory, PROPERTIES_ID, PROPERTIES_ID, "The task's properties", properties);
        addChild(removeCommand);
        addChild(errorValue);
        addChild(executingValue);
        addChild(propertyList);
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
    public String getError() {
        return errorValue.getTypedValue();
    }

    @Override
    public RealValue<Boolean> getExecutingValue() {
        return executingValue;
    }

    @Override
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
            for(TaskListener listener : getObjectListeners())
                listener.taskExecuting(this, executing);
        }
    }

    /**
     * Executes this task
     * @throws HousemateException if execution fails
     */
    public final void executeTask() throws HousemateException {
        getLog().d("Performing task " + getId());
        taskExecuting(true);
        execute();
        taskExecuting(false);
    }

    /**
     * Does the actual task execution
     * @throws HousemateException if execution fails
     */
    protected abstract void execute() throws HousemateException;
}

package com.intuso.housemate.object.server.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.task.TaskListener;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

import java.util.List;

public abstract class ServerRealTask
        extends ServerRealObject<
                    TaskData,
                    HousemateData<?>,
        ServerRealObject<?, ?, ?, ?>,
                    TaskListener<? super ServerRealTask>>
        implements Task<
        ServerRealCommand,
        ServerRealValue<Boolean>,
        ServerRealValue<String>,
        ServerRealList<PropertyData, ServerRealProperty<?>>, ServerRealTask> {

    private ServerRealCommand removeCommand;
    private ServerRealValue<String> errorValue;
    private ServerRealValue<Boolean> executingValue;
    private ServerRealList<PropertyData, ServerRealProperty<?>> propertyList;

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     */
    public ServerRealTask(ServerRealResources resources, String id, String name, String description,
                          ServerRealTaskOwner owner, ServerRealProperty<?>... properties) {
        this(resources, id, name, description, owner, Lists.newArrayList(properties));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param properties the task's properties
     */
    public ServerRealTask(ServerRealResources resources, String id, String name, String description,
                          final ServerRealTaskOwner owner, java.util.List<ServerRealProperty<?>> properties) {
        super(resources, new TaskData(id, name, description));
        removeCommand = new ServerRealCommand(resources, REMOVE_ID, REMOVE_ID, "Remove the task", Lists.<ServerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                owner.remove(ServerRealTask.this);
            }
        };
        errorValue = new ServerRealValue<String>(resources, ERROR_ID, ERROR_ID, "The current error", new StringType(resources.getRealResources()), (List)null);
        executingValue = new ServerRealValue<Boolean>(resources, EXECUTING_ID, EXECUTING_ID, "Whether the task is executing", new BooleanType(resources.getRealResources()), false);
        propertyList = new ServerRealList<PropertyData, ServerRealProperty<?>>(resources, PROPERTIES_ID, PROPERTIES_ID, "The task's properties", properties);
        addChild(removeCommand);
        addChild(errorValue);
        addChild(executingValue);
        addChild(propertyList);
    }

    @Override
    public ServerRealCommand getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ServerRealList<PropertyData, ServerRealProperty<?>> getProperties() {
        return propertyList;
    }

    @Override
    public ServerRealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public String getError() {
        return errorValue.getTypedValue();
    }

    @Override
    public ServerRealValue<Boolean> getExecutingValue() {
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
        getErrorValue().setTypedValue(error);
    }

    /**
     * Sets the executing value for this task
     * @param executing the executing value for this task
     */
    private void taskExecuting(boolean executing) {
        if(executing != isExecuting()) {
            getExecutingValue().setTypedValue(executing);
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

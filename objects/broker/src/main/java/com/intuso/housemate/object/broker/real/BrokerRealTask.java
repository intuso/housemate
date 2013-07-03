package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskListener;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

import java.util.ArrayList;
import java.util.List;

public abstract class BrokerRealTask
        extends BrokerRealObject<TaskWrappable, HousemateObjectWrappable<?>, BrokerRealObject<?, ?, ?, ?>,
        TaskListener<? super BrokerRealTask>>
        implements Task<BrokerRealValue<Boolean>, BrokerRealValue<String>,
                    BrokerRealList<PropertyWrappable, BrokerRealProperty<?>>, BrokerRealTask> {

    private BrokerRealValue<String> errorValue;
    private BrokerRealValue<Boolean> executingValue;
    private BrokerRealList<PropertyWrappable, BrokerRealProperty<?>> propertyList;

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     */
    public BrokerRealTask(BrokerRealResources resources, String id, String name, String description) {
        this(resources, id, name, description, new ArrayList<BrokerRealProperty<?>>(0));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param properties the task's properties
     */
    public BrokerRealTask(BrokerRealResources resources, String id, String name, String description, java.util.List<BrokerRealProperty<?>> properties) {
        super(resources, new TaskWrappable(id, name, description));
        errorValue = new BrokerRealValue<String>(resources, ERROR_ID, ERROR_ID, "The current error", new StringType(resources.getRealResources()), (List)null);
        executingValue = new BrokerRealValue<Boolean>(resources, EXECUTING_TYPE, EXECUTING_TYPE, "Whether the task is executing", new BooleanType(resources.getRealResources()), false);
        propertyList = new BrokerRealList<PropertyWrappable, BrokerRealProperty<?>>(resources, PROPERTIES_ID, PROPERTIES_ID, "The task's properties", properties);
        addWrapper(errorValue);
        addWrapper(executingValue);
        addWrapper(propertyList);
    }

    @Override
    public BrokerRealList<PropertyWrappable, BrokerRealProperty<?>> getProperties() {
        return propertyList;
    }

    @Override
    public BrokerRealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public String getError() {
        return errorValue.getTypedValue();
    }

    @Override
    public BrokerRealValue<Boolean> getExecutingValue() {
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

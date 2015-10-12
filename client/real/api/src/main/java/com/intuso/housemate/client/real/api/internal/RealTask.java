package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.real.api.internal.factory.task.TaskFactoryType;
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

public final class RealTask<DRIVER extends TaskDriver>
        extends RealObject<
        TaskData,
        HousemateData<?>,
        RealObject<?, ?, ?, ?>,
        Task.Listener<? super RealTask<DRIVER>>>
        implements Task<
        RealCommand,
        RealValue<Boolean>,
        RealValue<String>,
        RealProperty<TaskDriver.Factory<DRIVER>>,
        RealValue<Boolean>,
        RealList<PropertyData, RealProperty<?>>, RealTask<DRIVER>> {

    private RealCommand removeCommand;
    private RealValue<String> errorValue;
    private final RealProperty<TaskDriver.Factory<DRIVER>> driverProperty;
    private RealValue<Boolean> driverLoadedValue;
    private RealValue<Boolean> executingValue;
    private RealList<PropertyData, RealProperty<?>> propertyList;

    private DRIVER driver;

    /**
     * @param log {@inheritDoc}
     * @param data the object's data
     */
    @Inject
    public RealTask(Log log,
                    ListenersFactory listenersFactory,
                    TaskFactoryType driverFactoryType,
                    @Assisted TaskData data,
                    @Assisted final RemovedListener removedListener) {
        super(log, listenersFactory, data);
        removeCommand = new RealCommand(log, listenersFactory, TaskData.REMOVE_ID, TaskData.REMOVE_ID, "Remove the task", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                removedListener.taskRemoved(RealTask.this);
            }
        };
        errorValue = new RealValue<>(log, listenersFactory, TaskData.ERROR_ID, TaskData.ERROR_ID, "The current error", new StringType(log, listenersFactory), (List)null);
        driverProperty = (RealProperty<TaskDriver.Factory<DRIVER>>) new RealProperty(log, listenersFactory, "driver", "Driver", "The task's driver", driverFactoryType);
        driverLoadedValue = BooleanType.createValue(log, listenersFactory, TaskData.DRIVER_LOADED_ID, TaskData.DRIVER_LOADED_ID, "Whether the task's driver is loaded or not", false);
        executingValue = new RealValue<>(log, listenersFactory, TaskData.EXECUTING_ID, TaskData.EXECUTING_ID, "Whether the task is executing", new BooleanType(log, listenersFactory), false);
        propertyList = new RealList<>(log, listenersFactory, TaskData.PROPERTIES_ID, TaskData.PROPERTIES_ID, "The task's properties");
        addChild(removeCommand);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
        addChild(executingValue);
        addChild(propertyList);
    }

    public DRIVER getDriver() {
        return driver;
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
    public RealProperty<TaskDriver.Factory<DRIVER>> getDriverProperty() {
        return driverProperty;
    }

    @Override
    public RealValue<Boolean> getDriverLoadedValue() {
        return driverLoadedValue;
    }

    public boolean isDriverLoaded() {
        return driverLoadedValue.getTypedValue() != null ? driverLoadedValue.getTypedValue() : false;
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
            for(Task.Listener<? super RealTask<DRIVER>> listener : getObjectListeners())
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
    protected void execute() {
        // todo, call driver
    }

    public interface RemovedListener {
        void taskRemoved(RealTask task);
    }

    public interface Factory {
        RealTask<?> create(TaskData data, RemovedListener removedListener);
    }
}

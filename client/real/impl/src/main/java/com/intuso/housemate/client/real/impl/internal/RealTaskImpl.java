package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.bridge.v1_0.TaskDriverBridge;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.real.impl.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.impl.internal.factory.task.TaskFactoryType;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.TaskData;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.Task;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public final class RealTaskImpl<DRIVER extends TaskDriver>
        extends RealObject<
        TaskData,
        HousemateData<?>,
        RealObject<?, ?, ?, ?>,
        Task.Listener<? super RealTask<DRIVER>>>
        implements RealTask<DRIVER> {

    private RealCommandImpl removeCommand;
    private RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginResource<TaskDriver.Factory<DRIVER>>> driverProperty;
    private RealValueImpl<Boolean> driverLoadedValue;
    private RealValueImpl<Boolean> executingValue;
    private RealList<RealProperty<?>> properties;

    private final AnnotationProcessor annotationProcessor;

    private DRIVER driver;

    /**
     * @param log {@inheritDoc}
     * @param data the object's data
     */
    @Inject
    public RealTaskImpl(Log log,
                        ListenersFactory listenersFactory,
                        AnnotationProcessor annotationProcessor,
                        TaskFactoryType driverFactoryType,
                        @Assisted TaskData data,
                        @Assisted final RemoveCallback removeCallback) {
        super(log, listenersFactory, data);
        this.annotationProcessor = annotationProcessor;
        removeCommand = new RealCommandImpl(log, listenersFactory, TaskData.REMOVE_ID, TaskData.REMOVE_ID, "Remove the task", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                removeCallback.removeTask(RealTaskImpl.this);
            }
        };
        errorValue = new RealValueImpl<>(log, listenersFactory, TaskData.ERROR_ID, TaskData.ERROR_ID, "The current error", new StringType(log, listenersFactory), (List)null);
        driverProperty = (RealPropertyImpl<PluginResource<TaskDriver.Factory<DRIVER>>>) new RealPropertyImpl(log, listenersFactory, "driver", "Driver", "The task's driver", driverFactoryType);
        driverLoadedValue = BooleanType.createValue(log, listenersFactory, TaskData.DRIVER_LOADED_ID, TaskData.DRIVER_LOADED_ID, "Whether the task's driver is loaded or not", false);
        executingValue = new RealValueImpl<>(log, listenersFactory, TaskData.EXECUTING_ID, TaskData.EXECUTING_ID, "Whether the task is executing", new BooleanType(log, listenersFactory), false);
        properties = (RealList)new RealListImpl<>(log, listenersFactory, TaskData.PROPERTIES_ID, TaskData.PROPERTIES_ID, "The task's properties");
        addChild(removeCommand);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
        addChild(executingValue);
        addChild((RealListImpl)properties);
        driverProperty.addObjectListener(new Property.Listener<RealProperty<PluginResource<TaskDriver.Factory<DRIVER>>>>() {
            @Override
            public void valueChanging(RealProperty<PluginResource<TaskDriver.Factory<DRIVER>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealProperty<PluginResource<TaskDriver.Factory<DRIVER>>> factoryRealProperty) {
                initDriver();
            }
        });
        initDriver();
    }

    private void initDriver() {
        if(driver == null) {
            PluginResource<TaskDriver.Factory<DRIVER>> driverFactory = driverProperty.getTypedValue();
            if(driverFactory != null) {
                driver = driverFactory.getResource().create(this);
                for(RealProperty<?> property : annotationProcessor.findProperties(asOriginal(driver)))
                    properties.add(property);
                errorValue.setTypedValues((String) null);
                driverLoadedValue.setTypedValues(false);
            }
        }
    }

    private Object asOriginal(TaskDriver driver) {
        if(driver instanceof TaskDriverBridge)
            return ((TaskDriverBridge) driver).getTaskDriver();
        return driver;
    }

    private void uninitDriver() {
        if(driver != null) {
            driverLoadedValue.setTypedValues(false);
            errorValue.setTypedValues("Driver not loaded");
            driver = null;
            for (RealProperty<?> property : Lists.newArrayList(properties))
                properties.remove(property.getId());
        }
    }

    public DRIVER getDriver() {
        return driver;
    }

    @Override
    public RealCommand getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealList<RealProperty<?>> getProperties() {
        return properties;
    }

    @Override
    public RealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public RealProperty<PluginResource<TaskDriver.Factory<DRIVER>>> getDriverProperty() {
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
            for(Listener<? super RealTaskImpl<DRIVER>> listener : getObjectListeners())
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
}

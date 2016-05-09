package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.UsesDriver;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.real.api.internal.RealTask;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.real.impl.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.impl.internal.factory.task.TaskFactoryType;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * Base class for all task
 */
public final class RealTaskImpl<DRIVER extends TaskDriver>
        extends RealObject<Task.Data, Task.Listener<? super RealTaskImpl<DRIVER>>>
        implements RealTask<DRIVER, RealCommandImpl, RealValueImpl<Boolean>, RealValueImpl<String>,
                RealPropertyImpl<PluginResource<TaskDriver.Factory<DRIVER>>>, RealListImpl<RealPropertyImpl<?>>,
                RealTaskImpl<DRIVER>> {

    private final static String PROPERTIES_DESCRIPTION = "The task's properties";

    private final AnnotationProcessor annotationProcessor;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginResource<TaskDriver.Factory<DRIVER>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealListImpl<RealPropertyImpl<?>> properties;
    private final RealValueImpl<Boolean> executingValue;

    private final RemoveCallback<RealTaskImpl<DRIVER>> removeCallback;

    private DRIVER driver;

    /**
     * @param logger {@inheritDoc}
     * @param data the task's data
     * @param listenersFactory
     */
    @Inject
    public RealTaskImpl(@Assisted final Logger logger,
                        @Assisted Task.Data data,
                        ListenersFactory listenersFactory,
                        @Assisted RemoveCallback<RealTaskImpl<DRIVER>> removeCallback,
                        AnnotationProcessor annotationProcessor,
                        TaskFactoryType driverFactoryType) {
        super(logger, data, listenersFactory);
        this.annotationProcessor = annotationProcessor;
        this.removeCallback = removeCallback;
        this.renameCommand = new RealCommandImpl(ChildUtil.logger(logger, Renameable.RENAME_ID),
                new Command.Data(Renameable.RENAME_ID, Renameable.RENAME_ID, "Rename the task"),
                listenersFactory,
                StringType.createParameter(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        new Parameter.Data(Renameable.NAME_ID, Renameable.NAME_ID, "The new name"),
                        listenersFactory)) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                    String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                    if (newName != null && !RealTaskImpl.this.getName().equals(newName))
                        setName(newName);
                }
            }
        };
        this.removeCommand = new RealCommandImpl(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                new Command.Data(Removeable.REMOVE_ID, Removeable.REMOVE_ID, "Remove the task"),
                listenersFactory) {
            @Override
            public void perform(Type.InstanceMap values) {
                remove();
            }
        };
        this.errorValue = StringType.createValue(ChildUtil.logger(logger, Failable.ERROR_ID),
                new Value.Data(Failable.ERROR_ID, Failable.ERROR_ID, "Current error for the task"),
                listenersFactory,
                null);
        this.driverProperty = (RealPropertyImpl) new RealPropertyImpl(ChildUtil.logger(logger, UsesDriver.DRIVER_ID),
                new Property.Data(UsesDriver.DRIVER_ID, UsesDriver.DRIVER_ID, "The task's driver"),
                listenersFactory,
                driverFactoryType);
        this.driverLoadedValue = BooleanType.createValue(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID),
                new Value.Data(UsesDriver.DRIVER_LOADED_ID, UsesDriver.DRIVER_LOADED_ID, "Whether the task's driver is loaded or not"),
                listenersFactory,
                false);
        this.properties = new RealListImpl<>(ChildUtil.logger(logger, Task.PROPERTIES_ID), new List.Data(Task.PROPERTIES_ID, Task.PROPERTIES_ID, PROPERTIES_DESCRIPTION), listenersFactory);
        this.executingValue = BooleanType.createValue(ChildUtil.logger(logger, Task.EXECUTING_ID),
                new Value.Data(Task.EXECUTING_ID, Task.EXECUTING_ID, "Whether the task is executing or not"),
                listenersFactory,
                false);
        driverProperty.addObjectListener(new Property.Listener<RealPropertyImpl<PluginResource<TaskDriver.Factory<DRIVER>>>>() {
            @Override
            public void valueChanging(RealPropertyImpl<PluginResource<TaskDriver.Factory<DRIVER>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealPropertyImpl<PluginResource<TaskDriver.Factory<DRIVER>>> factoryRealProperty) {
                initDriver();
            }
        });
        initDriver();
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), session);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), session);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), session);
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), session);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), session);
        properties.init(ChildUtil.name(name, Task.PROPERTIES_ID), session);
        executingValue.init(ChildUtil.name(name, Task.EXECUTING_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        errorValue.uninit();
        driverProperty.uninit();
        driverLoadedValue.uninit();
        properties.uninit();
        executingValue.uninit();
    }

    private void setName(String newName) {
        RealTaskImpl.this.getData().setName(newName);
        for(Task.Listener<? super RealTaskImpl<DRIVER>> listener : listeners)
            listener.renamed(RealTaskImpl.this, RealTaskImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    private void initDriver() {
        if(driver == null) {
            PluginResource<TaskDriver.Factory<DRIVER>> driverFactory = driverProperty.getValue();
            if(driverFactory != null) {
                driver = driverFactory.getResource().create(logger, this);
                for(RealPropertyImpl<?> property : annotationProcessor.findProperties(logger, driver))
                    properties.add(property);
                errorValue.setValue((String) null);
                driverLoadedValue.setValue(true);
                _start();
            }
        }
    }

    private void uninitDriver() {
        if(driver != null) {
            _stop();
            driverLoadedValue.setValue(false);
            errorValue.setValue("Driver not loaded");
            driver = null;
            for (RealPropertyImpl<?> property : Lists.newArrayList(properties))
                properties.remove(property.getId());
        }
    }

    @Override
    public DRIVER getDriver() {
        return driver;
    }

    @Override
    public RealCommandImpl getRenameCommand() {
        return renameCommand;
    }

    @Override
    public RealCommandImpl getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealValueImpl<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public RealPropertyImpl<PluginResource<TaskDriver.Factory<DRIVER>>> getDriverProperty() {
        return driverProperty;
    }

    @Override
    public RealValueImpl<Boolean> getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public boolean isDriverLoaded() {
        return driverLoadedValue.getValue() != null ? driverLoadedValue.getValue() : false;
    }

    @Override
    public final RealListImpl<RealPropertyImpl<?>> getProperties() {
        return properties;
    }

    @Override
    public RealValueImpl<Boolean> getExecutingValue() {
        return executingValue;
    }

    @Override
    public boolean isExecuting() {
        return executingValue.getValue() != null ? executingValue.getValue() : false;
    }

    protected final void remove() {
        removeCallback.removeTask(this);
    }

    protected final void _start() {
        try {
            if(isDriverLoaded())
                driver.start();
        } catch (Throwable t) {
            getErrorValue().setValue("Could not start task: " + t.getMessage());
        }
    }

    protected final void _stop() {
        if(isDriverLoaded())
            driver.stop();
    }

    @Override
    public void setError(String error) {
        errorValue.setValue(error);
    }

    public final void start() {
        if(isDriverLoaded())
            driver.start();
    }


    public final void stop() {
        if(isDriverLoaded())
            driver.stop();
    }

    /**
     * Sets the executing value for this task
     * @param executing the executing value for this task
     */
    private void taskExecuting(boolean executing) {
        if(executing != isExecuting()) {
            executingValue.setValue(executing);
            for(Task.Listener<? super RealTaskImpl<DRIVER>> listener : listeners)
                listener.taskExecuting(this, executing);
        }
    }

    /**
     * Executes this task
     */
    @Override
    public final void executeTask() {
        logger.debug("Performing task " + getId());
        taskExecuting(true);
        execute();
        taskExecuting(false);
    }

    /**
     * Does the actual task execution
     */
    protected void execute() {
        if(isDriverLoaded())
            driver.execute();
    }

    public interface Factory {
        RealTaskImpl<?> create(Logger logger, Task.Data data, RemoveCallback<RealTaskImpl<?>> removeCallback);
    }
}

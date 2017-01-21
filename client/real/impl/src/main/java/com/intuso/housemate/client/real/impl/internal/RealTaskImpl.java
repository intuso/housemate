package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.bridge.v1_0.driver.TaskDriverBridge;
import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.UsesDriver;
import com.intuso.housemate.client.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Task;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.api.internal.RealTask;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParser;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Base class for all task
 */
public final class RealTaskImpl
        extends RealObject<Task.Data, Task.Listener<? super RealTaskImpl>>
        implements RealTask<RealCommandImpl, RealValueImpl<Boolean>, RealValueImpl<String>,
        RealPropertyImpl<PluginDependency<TaskDriver.Factory<?>>>, RealListGeneratedImpl<RealPropertyImpl<?>>,
        RealTaskImpl> {

    private final static String PROPERTIES_DESCRIPTION = "The task's properties";

    private final AnnotationParser annotationParser;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginDependency<TaskDriver.Factory<?>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealListGeneratedImpl<RealPropertyImpl<?>> properties;
    private final RealValueImpl<Boolean> executingValue;

    private final RemoveCallback<RealTaskImpl> removeCallback;

    private TaskDriver driver;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     */
    @Inject
    public RealTaskImpl(@Assisted final Logger logger,
                        @Assisted("id") String id,
                        @Assisted("name") String name,
                        @Assisted("description") String description,
                        @Assisted RemoveCallback<RealTaskImpl> removeCallback,
                        ListenersFactory listenersFactory,
                        AnnotationParser annotationParser,
                        RealCommandImpl.Factory commandFactory,
                        RealParameterImpl.Factory parameterFactory,
                        RealPropertyImpl.Factory propertyFactory,
                        RealValueImpl.Factory valueFactory,
                        RealListGeneratedImpl.Factory<RealPropertyImpl<?>> propertiesFactory,
                        TypeRepository typeRepository) {
        super(logger, new Task.Data(id, name, description), listenersFactory);
        this.annotationParser = annotationParser;
        this.removeCallback = removeCallback;
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the task",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                            String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                            if (newName != null && !RealTaskImpl.this.getName().equals(newName))
                                setName(newName);
                        }
                    }
                },
                Lists.newArrayList(parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        Renameable.NAME_ID,
                        Renameable.NAME_ID,
                        "The new name",
                        typeRepository.getType(new TypeSpec(String.class)),
                        1,
                        1)));
        this.removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                Removeable.REMOVE_ID,
                Removeable.REMOVE_ID,
                "Remove the task",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        remove();
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.errorValue = (RealValueImpl<String>) valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID),
                Failable.ERROR_ID,
                Failable.ERROR_ID,
                "Current error for the task",
                typeRepository.getType(new TypeSpec(String.class)),
                1,
                1,
                Lists.<String>newArrayList());
        this.driverProperty = (RealPropertyImpl<PluginDependency<TaskDriver.Factory<?>>>) propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID),
                UsesDriver.DRIVER_ID,
                UsesDriver.DRIVER_ID,
                "The task's driver",
                typeRepository.getType(new TypeSpec(Types.newParameterizedType(PluginDependency.class, TaskDriver.class))),
                1,
                1,
                Lists.<PluginDependency<TaskDriver.Factory<?>>>newArrayList());
        this.driverLoadedValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID),
                UsesDriver.DRIVER_LOADED_ID,
                UsesDriver.DRIVER_LOADED_ID,
                "Whether the task's driver is loaded or not",
                typeRepository.getType(new TypeSpec(Boolean.class)),
                1,
                1,
                Lists.newArrayList(false));
        this.properties = propertiesFactory.create(ChildUtil.logger(logger, Task.PROPERTIES_ID),
                Task.PROPERTIES_ID,
                Task.PROPERTIES_ID,
                PROPERTIES_DESCRIPTION,
                Lists.<RealPropertyImpl<?>>newArrayList());
        this.executingValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, Task.EXECUTING_ID),
                Task.EXECUTING_ID,
                Task.EXECUTING_ID,
                "Whether the task is executing or not",
                typeRepository.getType(new TypeSpec(Boolean.class)),
                1,
                1,
                Lists.newArrayList(false));
        driverProperty.addObjectListener(new Property.Listener<RealPropertyImpl<PluginDependency<TaskDriver.Factory<?>>>>() {
            @Override
            public void valueChanging(RealPropertyImpl<PluginDependency<TaskDriver.Factory<?>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealPropertyImpl<PluginDependency<TaskDriver.Factory<?>>> factoryRealProperty) {
                initDriver();
            }
        });
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), connection);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), connection);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), connection);
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), connection);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), connection);
        properties.init(ChildUtil.name(name, Task.PROPERTIES_ID), connection);
        executingValue.init(ChildUtil.name(name, Task.EXECUTING_ID), connection);
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
        for(Task.Listener<? super RealTaskImpl> listener : listeners)
            listener.renamed(RealTaskImpl.this, RealTaskImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    private void initDriver() {
        if(driver == null) {
            PluginDependency<TaskDriver.Factory<?>> driverFactory = driverProperty.getValue();
            if(driverFactory != null) {
                driver = driverFactory.getDependency().create(logger, this);
                Object annotatedObject;
                if(driver instanceof TaskDriverBridge)
                    annotatedObject = ((TaskDriverBridge) driver).getTaskDriver();
                else
                    annotatedObject = driver;
                for(RealPropertyImpl<?> property : annotationParser.findProperties(logger, "", annotatedObject))
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
    public <DRIVER extends TaskDriver> DRIVER getDriver() {
        return (DRIVER) driver;
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
    public RealPropertyImpl<PluginDependency<TaskDriver.Factory<?>>> getDriverProperty() {
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
    public final RealListGeneratedImpl<RealPropertyImpl<?>> getProperties() {
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
                driver.init(logger, this);
        } catch (Throwable t) {
            getErrorValue().setValue("Could not start task: " + t.getMessage());
        }
    }

    protected final void _stop() {
        if(isDriverLoaded())
            driver.uninit();
    }

    @Override
    public void setError(String error) {
        errorValue.setValue(error);
    }

    /**
     * Sets the executing value for this task
     * @param executing the executing value for this task
     */
    private void taskExecuting(boolean executing) {
        if(executing != isExecuting()) {
            executingValue.setValue(executing);
            for(Task.Listener<? super RealTaskImpl> listener : listeners)
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
        RealTaskImpl create(Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            RemoveCallback<RealTaskImpl> removeCallback);
    }
}

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
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.object.view.*;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealTask;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParser;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * Base class for all task
 */
public final class RealTaskImpl
        extends RealObject<Task.Data, Task.Listener<? super RealTaskImpl>, TaskView>
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

    private final RealListPersistedImpl.RemoveCallback<RealTaskImpl> removeCallback;

    private ManagedCollection.Registration driverAvailableListenerRegsitration;
    private TaskDriver driver;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealTaskImpl(@Assisted final Logger logger,
                        @Assisted("id") String id,
                        @Assisted("name") String name,
                        @Assisted("description") String description,
                        @Assisted RealListPersistedImpl.RemoveCallback<RealTaskImpl> removeCallback,
                        ManagedCollectionFactory managedCollectionFactory,
                        AnnotationParser annotationParser,
                        RealCommandImpl.Factory commandFactory,
                        RealParameterImpl.Factory parameterFactory,
                        RealPropertyImpl.Factory propertyFactory,
                        RealValueImpl.Factory valueFactory,
                        RealListGeneratedImpl.Factory<RealPropertyImpl<?>> propertiesFactory,
                        TypeRepository typeRepository) {
        super(logger, new Task.Data(id, name, description), managedCollectionFactory);
        this.annotationParser = annotationParser;
        this.removeCallback = removeCallback;
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the task",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(values != null) {
                            String newName = values.containsKey(Renameable.NAME_ID) ? values.get(Renameable.NAME_ID).getFirstValue() : data.getName();
                            String newDescription = values.containsKey(Renameable.DESCRIPTION_ID) ? values.get(Renameable.DESCRIPTION_ID).getFirstValue() : data.getDescription();
                            if (!(data.getName().equals(newName) && data.getDescription().equals(newDescription)))
                                rename(newName, newDescription);
                        }
                    }
                },
                Lists.newArrayList(
                        parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                                Renameable.NAME_ID,
                                Renameable.NAME_ID,
                                "The new name",
                                typeRepository.getType(new TypeSpec(String.class)),
                                1,
                                1),
                        parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.DESCRIPTION_ID),
                                Renameable.DESCRIPTION_ID,
                                Renameable.DESCRIPTION_ID,
                                "The new description",
                                typeRepository.getType(new TypeSpec(String.class)),
                                1,
                                1)
                ));
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
                uninitDriverListener();
            }

            @Override
            public void valueChanged(RealPropertyImpl<PluginDependency<TaskDriver.Factory<?>>> property) {
                if(property.getValue() != null) {
                    initDriverListener();
                    if (property.getValue().getDependency() != null)
                        initDriver(property.getValue().getDependency());
                }
            }
        });
    }

    @Override
    public TaskView createView(View.Mode mode) {
        return new TaskView(mode);
    }

    @Override
    public Tree getTree(TaskView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(new ValueView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_ID, driverProperty.getTree(new PropertyView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(new ValueView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(PROPERTIES_ID, properties.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(EXECUTING_ID, executingValue.getTree(new ValueView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_ID, driverProperty.getTree(view.getDriverProperty(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(view.getDriverLoadedValue(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(PROPERTIES_ID, properties.getTree(view.getProperties(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(EXECUTING_ID, executingValue.getTree(view.getExecutingValue(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getRenameCommand() != null)
                        result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getRemoveCommand() != null)
                        result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getErrorValue() != null)
                        result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), referenceHandler, listener, listenerRegistrations));
                    if(view.getDriverProperty() != null)
                        result.getChildren().put(DRIVER_ID, driverProperty.getTree(view.getDriverProperty(), referenceHandler, listener, listenerRegistrations));
                    if(view.getDriverLoadedValue() != null)
                        result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(view.getDriverLoadedValue(), referenceHandler, listener, listenerRegistrations));
                    if(view.getProperties() != null)
                        result.getChildren().put(PROPERTIES_ID, properties.getTree(view.getProperties(), referenceHandler, listener, listenerRegistrations));
                    if(view.getExecutingValue() != null)
                        result.getChildren().put(EXECUTING_ID, executingValue.getTree(view.getExecutingValue(), referenceHandler, listener, listenerRegistrations));
                    break;
            }

        }

        return result;
    }

    private void initDriverListener() {
        PluginDependency<TaskDriver.Factory<?>> driverFactory = driverProperty.getValue();
        driverAvailableListenerRegsitration = driverFactory.addListener(new PluginDependency.Listener<TaskDriver.Factory<?>>() {
            @Override
            public void dependencyAvailable(TaskDriver.Factory<?> dependency) {
                initDriver(dependency);
            }

            @Override
            public void dependencyUnavailable() {
                uninitDriver();
            }
        });
    }

    private void uninitDriverListener() {
        if(driverAvailableListenerRegsitration != null) {
            driverAvailableListenerRegsitration.remove();
            driverAvailableListenerRegsitration = null;
        }
    }

    private void initDriver(TaskDriver.Factory<?> driverFactory) {
        if(driver != null)
            uninit();
        driver = driverFactory.create(logger, this);
        java.lang.Object annotatedObject;
        if(driver instanceof TaskDriverBridge)
            annotatedObject = ((TaskDriverBridge) driver).getTaskDriver();
        else
            annotatedObject = driver;
        for(RealPropertyImpl<?> property : annotationParser.findProperties(logger, annotatedObject))
            properties.add(property);
        errorValue.setValue(null);
        driverLoadedValue.setValue(true);
        driver.init(logger, this);
    }

    private void uninitDriver() {
        if(driver != null) {
            driver.uninit();
            driverLoadedValue.setValue(false);
            errorValue.setValue("Driver not loaded");
            driver = null;
            for (RealPropertyImpl<?> property : Lists.newArrayList(properties))
                properties.remove(property.getId());
        }
    }

    @Override
    protected void initChildren(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory) {
        super.initChildren(name, senderFactory, receiverFactory);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), senderFactory, receiverFactory);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), senderFactory, receiverFactory);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), senderFactory, receiverFactory);
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), senderFactory, receiverFactory);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), senderFactory, receiverFactory);
        properties.init(ChildUtil.name(name, Task.PROPERTIES_ID), senderFactory, receiverFactory);
        executingValue.init(ChildUtil.name(name, Task.EXECUTING_ID), senderFactory, receiverFactory);
    }

    @Override
    protected void uninitChildren() {
        _stop();
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        errorValue.uninit();
        driverProperty.uninit();
        driverLoadedValue.uninit();
        properties.uninit();
        executingValue.uninit();
    }

    private void rename(String newName, String newDescription) {
        getData().setName(newName);
        for(Task.Listener<? super RealTaskImpl> listener : listeners)
            listener.renamed(RealTaskImpl.this, data.getName(), data.getDescription(), newName, newDescription);
        data.setName(newName);
        data.setDescription(newDescription);
        dataUpdated();
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

    @Override
    public RealObject<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(REMOVE_ID.equals(id))
            return removeCommand;
        else if(ERROR_ID.equals(id))
            return errorValue;
        else if(DRIVER_ID.equals(id))
            return driverProperty;
        else if(DRIVER_LOADED_ID.equals(id))
            return driverLoadedValue;
        else if(PROPERTIES_ID.equals(id))
            return properties;
        else if(EXECUTING_ID.equals(id))
            return executingValue;
        return null;
    }

    protected final void remove() {
        removeCallback.remove(this);
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
                            RealListPersistedImpl.RemoveCallback<RealTaskImpl> removeCallback);
    }

    public static class LoadPersisted implements RealListPersistedImpl.ElementFactory<Task.Data, RealTaskImpl> {

        private final RealTaskImpl.Factory factory;

        @Inject
        public LoadPersisted(Factory factory) {
            this.factory = factory;
        }

        @Override
        public RealTaskImpl create(Logger logger, Task.Data data, RealListPersistedImpl.RemoveCallback<RealTaskImpl> removeCallback) {
            return factory.create(logger, data.getId(), data.getName(), data.getDescription(), removeCallback);
        }
    }
}

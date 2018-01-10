package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.object.view.*;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParser;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Base class for all hardwares
 */
public final class RealHardwareImpl
        extends RealObject<Hardware.Data, Hardware.Listener<? super RealHardwareImpl>, HardwareView>
        implements RealHardware<RealCommandImpl, RealValueImpl<Boolean>, RealValueImpl<String>,
        RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>>, RealListGeneratedImpl<RealCommandImpl>,
        RealListGeneratedImpl<RealValueImpl<?>>, RealListGeneratedImpl<RealPropertyImpl<?>>,
        RealListPersistedImpl<Device.Connected.Data, RealDeviceConnectedImpl>, RealHardwareImpl> {

    private final static String COMMANDS_DESCRIPTION = "The hardware's commands";
    private final static String VALUES_DESCRIPTION = "The hardware's values";
    private final static String PROPERTIES_DESCRIPTION = "The hardware's properties";
    private final static String DEVICES_DESCRIPTION = "The hardware's devices";

    private final AnnotationParser annotationParser;
    private final RealDeviceConnectedImpl.Factory deviceFactory;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<Boolean> runningValue;
    private final RealCommandImpl startCommand;
    private final RealCommandImpl stopCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealListGeneratedImpl<RealCommandImpl> commands;
    private final RealListGeneratedImpl<RealValueImpl<?>> values;
    private final RealListGeneratedImpl<RealPropertyImpl<?>> properties;
    private final RealListPersistedImpl<Device.Connected.Data, RealDeviceConnectedImpl> devices;

    private final RealListPersistedImpl.RemoveCallback<RealHardwareImpl> removeCallback;

    private final Map<java.lang.Object, RealDeviceConnectedImpl> objectDevices = Maps.newHashMap();

    private ManagedCollection.Registration driverAvailableListenerRegsitration;
    private HardwareDriver driver;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealHardwareImpl(@Assisted final Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            @Assisted RealListPersistedImpl.RemoveCallback<RealHardwareImpl> removeCallback,
                            ManagedCollectionFactory managedCollectionFactory,
                            Sender.Factory senderFactory,
                            AnnotationParser annotationParser,
                            RealCommandImpl.Factory commandFactory,
                            RealParameterImpl.Factory parameterFactory,
                            RealPropertyImpl.Factory propertyFactory,
                            RealValueImpl.Factory valueFactory,
                            TypeRepository typeRepository,
                            RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                            RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory,
                            RealListGeneratedImpl.Factory<RealPropertyImpl<?>> propertiesFactory,
                            final RealDeviceConnectedImpl.Factory deviceFactory,
                            RealListPersistedImpl.Factory<Device.Connected.Data, RealDeviceConnectedImpl> devicesFactory) {
        super(logger, new Hardware.Data(id, name, description), managedCollectionFactory, senderFactory);
        this.annotationParser = annotationParser;
        this.deviceFactory = deviceFactory;
        this.removeCallback = removeCallback;
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the hardware",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if (values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                            String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                            if (newName != null && !RealHardwareImpl.this.getName().equals(newName))
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
                "Remove the hardware",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(isRunning())
                            throw new HousemateException("Cannot remove while hardware is still running");
                        remove();
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.runningValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, Runnable.RUNNING_ID),
                Runnable.RUNNING_ID,
                Runnable.RUNNING_ID,
                "Whether the hardware is running or not",
                typeRepository.getType(new TypeSpec(Boolean.class)),
                1,
                1,
                Lists.newArrayList(false));
        this.startCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.START_ID),
                Runnable.START_ID,
                Runnable.START_ID,
                "Start the hardware",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(!isRunning()) {
                            startDriver();
                            runningValue.setValue(true);
                        }
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.stopCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.STOP_ID),
                Runnable.STOP_ID,
                Runnable.STOP_ID,
                "Stop the hardware",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(isRunning()) {
                            _stop();
                            runningValue.setValue(false);
                        }
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.errorValue = (RealValueImpl<String>) valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID),
                Failable.ERROR_ID,
                Failable.ERROR_ID,
                "Current error for the hardware",
                typeRepository.getType(new TypeSpec(String.class)),
                1,
                1,
                Lists.<String>newArrayList());
        this.driverProperty = (RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>>) propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID),
                UsesDriver.DRIVER_ID,
                UsesDriver.DRIVER_ID,
                "The hardware's driver",
                typeRepository.getType(new TypeSpec(Types.newParameterizedType(PluginDependency.class, HardwareDriver.class))),
                1,
                1,
                Lists.<PluginDependency<HardwareDriver.Factory<?>>>newArrayList());
        this.driverLoadedValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID),
                UsesDriver.DRIVER_LOADED_ID,
                UsesDriver.DRIVER_LOADED_ID,
                "Whether the hardware's driver is loaded or not",
                typeRepository.getType(new TypeSpec(Boolean.class)),
                1,
                1,
                Lists.newArrayList(false));
        this.commands = commandsFactory.create(ChildUtil.logger(logger, Hardware.COMMANDS_ID),
                Hardware.COMMANDS_ID,
                Hardware.COMMANDS_ID,
                COMMANDS_DESCRIPTION,
                Lists.<RealCommandImpl>newArrayList());
        this.values = valuesFactory.create(ChildUtil.logger(logger, Hardware.VALUES_ID),
                Hardware.VALUES_ID,
                Hardware.VALUES_ID,
                VALUES_DESCRIPTION,
                Lists.<RealValueImpl<?>>newArrayList());
        this.properties = propertiesFactory.create(ChildUtil.logger(logger, Hardware.PROPERTIES_ID),
                Hardware.PROPERTIES_ID,
                Hardware.PROPERTIES_ID,
                PROPERTIES_DESCRIPTION,
                Lists.<RealPropertyImpl<?>>newArrayList());
        this.devices = devicesFactory.create(ChildUtil.logger(logger, Hardware.DEVICES_ID),
                Hardware.DEVICES_ID,
                Hardware.DEVICES_ID,
                DEVICES_DESCRIPTION);
        driverProperty.addObjectListener(new Property.Listener<RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>>>() {
            @Override
            public void valueChanging(RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>> factoryRealProperty) {
                uninitDriver();
                uninitDriverListener();
            }

            @Override
            public void valueChanged(RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>> property) {
                if(property.getValue() != null) {
                    initDriverListener();
                    if (property.getValue().getDependency() != null)
                        initDriver(property.getValue().getDependency());
                }
            }
        });
    }

    @Override
    public HardwareView createView(View.Mode mode) {
        return new HardwareView(mode);
    }

    @Override
    public Tree getTree(HardwareView view, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(RUNNING_ID, runningValue.getTree(new ValueView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(START_ID, startCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(STOP_ID, stopCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(new ValueView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_ID, driverProperty.getTree(new PropertyView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(new ValueView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(COMMANDS_ID, commands.getTree(new ListView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(VALUES_ID, values.getTree(new ListView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(PROPERTIES_ID, properties.getTree(new ListView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(DEVICES_ID, devices.getTree(new ListView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), listener, listenerRegistrations));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), listener, listenerRegistrations));
                    result.getChildren().put(RUNNING_ID, runningValue.getTree(view.getRunningValue(), listener, listenerRegistrations));
                    result.getChildren().put(START_ID, startCommand.getTree(view.getStartCommand(), listener, listenerRegistrations));
                    result.getChildren().put(STOP_ID, stopCommand.getTree(view.getStopCommand(), listener, listenerRegistrations));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_ID, driverProperty.getTree(view.getDriverProperty(), listener, listenerRegistrations));
                    result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(view.getDriverLoadedValue(), listener, listenerRegistrations));
                    result.getChildren().put(COMMANDS_ID, commands.getTree(view.getCommands(), listener, listenerRegistrations));
                    result.getChildren().put(VALUES_ID, values.getTree(view.getValues(), listener, listenerRegistrations));
                    result.getChildren().put(PROPERTIES_ID, properties.getTree(view.getProperties(), listener, listenerRegistrations));
                    result.getChildren().put(DEVICES_ID, devices.getTree(view.getDevices(), listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getRenameCommand() != null)
                        result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), listener, listenerRegistrations));
                    if(view.getRemoveCommand() != null)
                        result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), listener, listenerRegistrations));
                    if(view.getRunningValue() != null)
                        result.getChildren().put(RUNNING_ID, runningValue.getTree(view.getRunningValue(), listener, listenerRegistrations));
                    if(view.getStartCommand() != null)
                        result.getChildren().put(START_ID, startCommand.getTree(view.getStartCommand(), listener, listenerRegistrations));
                    if(view.getStopCommand() != null)
                        result.getChildren().put(STOP_ID, stopCommand.getTree(view.getStopCommand(), listener, listenerRegistrations));
                    if(view.getErrorValue() != null)
                        result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), listener, listenerRegistrations));
                    if(view.getDriverProperty() != null)
                        result.getChildren().put(DRIVER_ID, driverProperty.getTree(view.getDriverProperty(), listener, listenerRegistrations));
                    if(view.getDriverLoadedValue() != null)
                        result.getChildren().put(DRIVER_LOADED_ID, driverLoadedValue.getTree(view.getDriverLoadedValue(), listener, listenerRegistrations));
                    if(view.getCommands() != null)
                        result.getChildren().put(COMMANDS_ID, commands.getTree(view.getCommands(), listener, listenerRegistrations));
                    if(view.getValues() != null)
                        result.getChildren().put(VALUES_ID, values.getTree(view.getValues(), listener, listenerRegistrations));
                    if(view.getProperties() != null)
                        result.getChildren().put(PROPERTIES_ID, properties.getTree(view.getProperties(), listener, listenerRegistrations));
                    if(view.getDevices() != null)
                        result.getChildren().put(DEVICES_ID, devices.getTree(view.getDevices(), listener, listenerRegistrations));
                    break;
            }

        }

        return result;
    }

    private void initDriverListener() {
        PluginDependency<HardwareDriver.Factory<?>> driverFactory = driverProperty.getValue();
        driverAvailableListenerRegsitration = driverFactory.addListener(new PluginDependency.Listener<HardwareDriver.Factory<?>>() {
            @Override
            public void dependencyAvailable(HardwareDriver.Factory<?> dependency) {
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

    private void initDriver(HardwareDriver.Factory<?> driverFactory) {
        if(driver != null)
            uninit();
        driver = driverFactory.create(logger, this);
        for(RealCommandImpl command : annotationParser.findCommands(logger, "", driver))
            commands.add(command);
        for(RealValueImpl<?> value : annotationParser.findValues(logger, "", driver))
            values.add(value);
        for(RealPropertyImpl<?> property : annotationParser.findProperties(logger, "", driver))
            properties.add(property);
        errorValue.setValue(null);
        driverLoadedValue.setValue(true);
        if(isRunning())
            startDriver();
    }

    private void uninitDriver() {
        // clear all the objects from the devices, but not the devices themselves
        for(RealDeviceConnectedImpl device : objectDevices.values())
            device.clear();
        objectDevices.clear();
        if(driver != null) {
            driver.uninit();
            driverLoadedValue.setValue(false);
            errorValue.setValue("Driver not loaded");
            driver = null;
            for (RealCommandImpl command : Lists.newArrayList(commands))
                commands.remove(command.getId());
            for (RealValueImpl<?> value : Lists.newArrayList(values))
                values.remove(value.getId());
            for (RealPropertyImpl<?> property : Lists.newArrayList(properties))
                properties.remove(property.getId());
        }
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID));
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID));
        runningValue.init(ChildUtil.name(name, Runnable.RUNNING_ID));
        stopCommand.init(ChildUtil.name(name, Runnable.STOP_ID));
        startCommand.init(ChildUtil.name(name, Runnable.START_ID));
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID));
        commands.init(ChildUtil.name(name, Hardware.COMMANDS_ID));
        values.init(ChildUtil.name(name, Hardware.VALUES_ID));
        properties.init(ChildUtil.name(name, Hardware.PROPERTIES_ID));
        devices.init(ChildUtil.name(name, Hardware.DEVICES_ID));
        // do driver last as it's better to have the devices loaded already
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID));
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID));
    }

    @Override
    protected void uninitChildren() {
        _stop();
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        runningValue.uninit();
        startCommand.uninit();
        stopCommand.uninit();
        errorValue.uninit();
        driverProperty.uninit();
        driverLoadedValue.uninit();
        commands.uninit();
        values.uninit();
        properties.uninit();
        devices.uninit();
    }

    private void setName(String newName) {
        RealHardwareImpl.this.getData().setName(newName);
        for(Hardware.Listener<? super RealHardwareImpl> listener : listeners)
            listener.renamed(RealHardwareImpl.this, RealHardwareImpl.this.getName(), newName);
        data.setName(newName);
        dataUpdated();
    }

    public <DRIVER extends HardwareDriver> DRIVER getDriver() {
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
    public RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>> getDriverProperty() {
        return driverProperty;
    }

    @Override
    public RealValueImpl<Boolean> getDriverLoadedValue() {
        return driverLoadedValue;
    }

    public boolean isDriverLoaded() {
        return driverLoadedValue.getValue() != null ? driverLoadedValue.getValue() : false;
    }

    @Override
    public RealCommandImpl getStopCommand() {
        return stopCommand;
    }

    @Override
    public RealCommandImpl getStartCommand() {
        return startCommand;
    }

    @Override
    public RealValueImpl<Boolean> getRunningValue() {
        return runningValue;
    }

    public boolean isRunning() {
        return runningValue.getValue() != null ? runningValue.getValue() : false;
    }

    protected final void remove() {
        removeCallback.remove(this);
    }

    @Override
    public RealListGeneratedImpl<RealCommandImpl> getCommands() {
        return commands;
    }

    @Override
    public RealListGeneratedImpl<RealValueImpl<?>> getValues() {
        return values;
    }

    @Override
    public final RealListGeneratedImpl<RealPropertyImpl<?>> getProperties() {
        return properties;
    }

    @Override
    public final RealListPersistedImpl<Device.Connected.Data, RealDeviceConnectedImpl> getDeviceConnecteds() {
        return devices;
    }

    @Override
    public RealObject<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(REMOVE_ID.equals(id))
            return removeCommand;
        else if(RUNNING_ID.equals(id))
            return runningValue;
        else if(START_ID.equals(id))
            return startCommand;
        else if(STOP_ID.equals(id))
            return stopCommand;
        else if(ERROR_ID.equals(id))
            return errorValue;
        else if(DRIVER_ID.equals(id))
            return driverProperty;
        else if(DRIVER_LOADED_ID.equals(id))
            return driverLoadedValue;
        else if(COMMANDS_ID.equals(id))
            return commands;
        else if(PROPERTIES_ID.equals(id))
            return properties;
        else if(VALUES_ID.equals(id))
            return values;
        else if(DEVICES_ID.equals(id))
            return devices;
        return null;
    }

    protected final void startDriver() {
        try {
            if(driver != null) {
                driver.init(logger, this);
                for (RealDeviceConnectedImpl device : devices)
                    driver.foundDeviceId(device.getId());
            }
        } catch (Throwable t) {
            getErrorValue().setValue("Could not start hardware: " + t.getMessage());
        }
    }

    protected final void _stop() {
        if(driver != null)
            driver.uninit();
    }

    @Override
    public void setError(String error) {
        errorValue.setValue(error);
    }

    @Override
    public void addDevice(String id, String name, String description, java.lang.Object object) {

        // check if we already know about the object
        if(objectDevices.containsKey(object))
            throw new HousemateException("Object is already added");

        // get the device by id
        RealDeviceConnectedImpl device = devices.get(id);

        // if it doesn't exist yet create it
        if(device == null) {
            device = deviceFactory.create(ChildUtil.logger(logger, Hardware.DEVICES_ID, id), id, name, description);
            devices.add(device);
        }

        // else remove what it already has
        device.wrap(object);
    }

    @Override
    public void removeDevice(java.lang.Object object) {
        RealDeviceConnectedImpl device = objectDevices.remove(object);
        if(device != null)
            devices.remove(device.getId());
    }

    public interface Factory {
        RealHardwareImpl create(Logger logger,
                                @Assisted("id") String id,
                                @Assisted("name") String name,
                                @Assisted("description") String description,
                                RealListPersistedImpl.RemoveCallback<RealHardwareImpl> removeCallback);
    }

    public static class LoadPersisted implements RealListPersistedImpl.ElementFactory<Hardware.Data, RealHardwareImpl> {

        private final RealHardwareImpl.Factory factory;

        @Inject
        public LoadPersisted(Factory factory) {
            this.factory = factory;
        }

        @Override
        public RealHardwareImpl create(Logger logger, Hardware.Data data, RealListPersistedImpl.RemoveCallback<RealHardwareImpl> removeCallback) {
            return factory.create(logger, data.getId(), data.getName(), data.getDescription(), removeCallback);
        }
    }
}

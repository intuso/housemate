package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.client.real.impl.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.impl.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.Device;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Base class for all devices
 */
public final class RealDeviceImpl<DRIVER extends DeviceDriver>
        extends RealObject<
        DeviceData,
        HousemateData<?>,
        RealObject<?, ?, ?, ?>,
        Device.Listener<? super RealDevice<DRIVER>>>
        implements RealDevice<DRIVER> {

    private final static String COMMANDS_DESCRIPTION = "The device's commands";
    private final static String VALUES_DESCRIPTION = "The device's values";
    private final static String PROPERTIES_DESCRIPTION = "The device's properties";

    private final AnnotationProcessor annotationProcessor;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<Boolean> runningValue;
    private final RealCommandImpl startCommand;
    private final RealCommandImpl stopCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginResource<DeviceDriver.Factory<DRIVER>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealListImpl<CommandData, RealCommandImpl> commands;
    private final RealListImpl<ValueData, RealValueImpl<?>> values;
    private final RealListImpl<PropertyData, RealPropertyImpl<?>> properties;

    private final RemovedListener removedListener;

    private DRIVER driver;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data the device's data
     */
    @Inject
    public RealDeviceImpl(Log log,
                          ListenersFactory listenersFactory,
                          AnnotationProcessor annotationProcessor,
                          DeviceFactoryType driverFactoryType,
                          @Assisted DeviceData data,
                          @Assisted RemovedListener removedListener) {
        super(log, listenersFactory, new DeviceData(data.getId(), data.getName(), data.getDescription()));
        this.annotationProcessor = annotationProcessor;
        this.removedListener = removedListener;
        this.renameCommand = new RealCommandImpl(log, listenersFactory, DeviceData.RENAME_ID, DeviceData.RENAME_ID, "Rename the device", Lists.<RealParameterImpl<?>>newArrayList(StringType.createParameter(log, listenersFactory, DeviceData.NAME_ID, DeviceData.NAME_ID, "The new name"))) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(values != null && values.getChildren().containsKey(DeviceData.NAME_ID)) {
                    String newName = values.getChildren().get(DeviceData.NAME_ID).getFirstValue();
                    if (newName != null && !RealDeviceImpl.this.getName().equals(newName)) {
                        RealDeviceImpl.this.getData().setName(newName);
                        for(Device.Listener<? super RealDeviceImpl<DRIVER>> listener : RealDeviceImpl.this.getObjectListeners())
                            listener.renamed(RealDeviceImpl.this, RealDeviceImpl.this.getName(), newName);
                        RealDeviceImpl.this.sendMessage(DeviceData.NEW_NAME, new StringPayload(newName));
                    }
                }
            }
        };
        this.removeCommand = new RealCommandImpl(log, listenersFactory, DeviceData.REMOVE_ID, DeviceData.REMOVE_ID, "Remove the device", Lists.<RealParameterImpl<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning())
                    throw new HousemateCommsException("Cannot remove while device is still running");
                remove();
            }
        };
        this.runningValue = BooleanType.createValue(log, listenersFactory, DeviceData.RUNNING_ID, DeviceData.RUNNING_ID, "Whether the device is running or not", false);
        this.startCommand = new RealCommandImpl(log, listenersFactory, DeviceData.START_ID, DeviceData.START_ID, "Start the device", Lists.<RealParameterImpl<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(!isRunning()) {
                    _start();
                    runningValue.setTypedValues(true);
                }
            }
        };
        this.stopCommand = new RealCommandImpl(log, listenersFactory, DeviceData.STOP_ID, DeviceData.STOP_ID, "Stop the device", Lists.<RealParameterImpl<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning()) {
                    _stop();
                    runningValue.setTypedValues(false);
                }
            }
        };
        this.errorValue = StringType.createValue(log, listenersFactory, DeviceData.ERROR_ID, DeviceData.ERROR_ID, "Current error for the device", null);
        this.driverProperty = (RealPropertyImpl<PluginResource<DeviceDriver.Factory<DRIVER>>>) new RealPropertyImpl(log, listenersFactory, "driver", "Driver", "The device's driver", driverFactoryType);
        this.driverLoadedValue = BooleanType.createValue(log, listenersFactory, DeviceData.DRIVER_LOADED_ID, DeviceData.DRIVER_LOADED_ID, "Whether the device's driver is loaded or not", false);
        this.commands = new RealListImpl<>(log, listenersFactory, DeviceData.COMMANDS_ID, DeviceData.COMMANDS_ID, COMMANDS_DESCRIPTION);
        this.values = new RealListImpl<>(log, listenersFactory, DeviceData.VALUES_ID, DeviceData.VALUES_ID, VALUES_DESCRIPTION);
        this.properties = new RealListImpl<>(log, listenersFactory, DeviceData.PROPERTIES_ID, DeviceData.PROPERTIES_ID, PROPERTIES_DESCRIPTION);
        addChild(renameCommand);
        addChild(removeCommand);
        addChild(runningValue);
        addChild(startCommand);
        addChild(stopCommand);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
        addChild(commands);
        addChild(values);
        addChild(properties);
        driverProperty.addObjectListener(new Property.Listener<RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>>>() {
            @Override
            public void valueChanging(RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>> factoryRealProperty) {
                initDriver();
            }
        });
        initDriver();
    }

    private void initDriver() {
        if(driver == null) {
            PluginResource<DeviceDriver.Factory<DRIVER>> driverFactory = driverProperty.getTypedValue();
            if(driverFactory != null) {
                driver = driverFactory.getResource().create(this);
                annotationProcessor.process(this, driver);
                errorValue.setTypedValues((String) null);
                driverLoadedValue.setTypedValues(false);
                _start();
            }
        }
    }

    private void uninitDriver() {
        if(driver != null) {
            _stop();
            driverLoadedValue.setTypedValues(false);
            errorValue.setTypedValues("Driver not loaded");
            driver = null;
            for (RealCommand command : Lists.newArrayList(commands.getChildren()))
                commands.remove(command.getId());
            for (RealValue<?> value : Lists.newArrayList(values.getChildren()))
                values.remove(value.getId());
            for (RealProperty<?> property : Lists.newArrayList(properties.getChildren()))
                properties.remove(property.getId());
        }
    }

    public DRIVER getDriver() {
        return driver;
    }

    @Override
    public RealCommand getRenameCommand() {
        return renameCommand;
    }

    @Override
    public RealCommand getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealValue<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>> getDriverProperty() {
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
    public RealCommand getStopCommand() {
        return stopCommand;
    }

    @Override
    public RealCommand getStartCommand() {
        return startCommand;
    }

    @Override
    public RealValue<Boolean> getRunningValue() {
        return runningValue;
    }

    public boolean isRunning() {
        return runningValue.getTypedValue() != null ? runningValue.getTypedValue() : false;
    }

    @Override
    public final RealList<? extends RealCommand> getCommands() {
        return commands;
    }

    @Override
    public final RealList<? extends RealValue<?>> getValues() {
        return values;
    }

    @Override
    public final RealList<? extends RealProperty<?>> getProperties() {
        return properties;
    }

    protected final void remove() {
        removedListener.deviceRemoved(this);
    }

    protected final void _start() {
        try {
            if(driver != null)
                driver.start();
        } catch (Throwable t) {
            getErrorValue().setTypedValues("Could not start device: " + t.getMessage());
        }
    }

    protected final void _stop() {
        if(driver != null)
            driver.stop();
    }

    @Override
    public final List<String> getFeatureIds() {
        return getData().getFeatureIds();
    }

    @Override
    public final List<String> getCustomCommandIds() {
        return getData().getCustomCommandIds();
    }

    @Override
    public final List<String> getCustomValueIds() {
        return getData().getCustomValueIds();
    }

    @Override
    public final List<String> getCustomPropertyIds() {
        return getData().getCustomPropertyIds();
    }

    @Override
    public void setError(String error) {
        errorValue.setTypedValues(error);
    }
}

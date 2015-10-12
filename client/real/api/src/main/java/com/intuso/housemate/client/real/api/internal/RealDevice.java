package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.api.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.api.internal.impl.type.BooleanType;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.Device;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Base class for all devices
 */
public final class RealDevice<DRIVER extends DeviceDriver>
        extends RealObject<
        DeviceData,
        HousemateData<?>,
        RealObject<?, ?, ?, ?>,
        Device.Listener<? super RealDevice<DRIVER>>>
        implements Device<
        RealCommand,
        RealCommand,
        RealCommand,
        RealCommand,
        RealList<CommandData, RealCommand>,
        RealValue<Boolean>,
        RealValue<String>,
        RealProperty<DeviceDriver.Factory<DRIVER>>,
        RealValue<Boolean>,
        RealValue<?>,
        RealList<ValueData, RealValue<?>>,
        RealProperty<?>,
        RealList<PropertyData, RealProperty<?>>,
        RealDevice<DRIVER>> {

    private final static String COMMANDS_DESCRIPTION = "The device's commands";
    private final static String VALUES_DESCRIPTION = "The device's values";
    private final static String PROPERTIES_DESCRIPTION = "The device's properties";

    private final RealCommand renameCommand;
    private final RealCommand removeCommand;
    private final RealValue<Boolean> runningValue;
    private final RealCommand startCommand;
    private final RealCommand stopCommand;
    private final RealValue<String> errorValue;
    private final RealProperty<DeviceDriver.Factory<DRIVER>> driverProperty;
    private final RealValue<Boolean> driverLoadedValue;
    private final RealList<CommandData, RealCommand> commands;
    private final RealList<ValueData, RealValue<?>> values;
    private final RealList<PropertyData, RealProperty<?>> properties;

    private final RemovedListener removedListener;

    private DRIVER driver;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data the device's data
     */
    @Inject
    public RealDevice(Log log,
                      ListenersFactory listenersFactory,
                      DeviceFactoryType driverFactoryType,
                      @Assisted DeviceData data,
                      @Assisted RemovedListener removedListener) {
        super(log, listenersFactory, new DeviceData(data.getId(), data.getName(), data.getDescription()));
        this.removedListener = removedListener;
        this.renameCommand = new RealCommand(log, listenersFactory, DeviceData.RENAME_ID, DeviceData.RENAME_ID, "Rename the device", Lists.<RealParameter<?>>newArrayList(StringType.createParameter(log, listenersFactory, DeviceData.NAME_ID, DeviceData.NAME_ID, "The new name"))) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(values != null && values.getChildren().containsKey(DeviceData.NAME_ID)) {
                    String newName = values.getChildren().get(DeviceData.NAME_ID).getFirstValue();
                    if (newName != null && !RealDevice.this.getName().equals(newName)) {
                        RealDevice.this.getData().setName(newName);
                        for(Device.Listener<? super RealDevice<DRIVER>> listener : RealDevice.this.getObjectListeners())
                            listener.renamed(RealDevice.this, RealDevice.this.getName(), newName);
                        RealDevice.this.sendMessage(DeviceData.NEW_NAME, new StringPayload(newName));
                    }
                }
            }
        };
        this.removeCommand = new RealCommand(log, listenersFactory, DeviceData.REMOVE_ID, DeviceData.REMOVE_ID, "Remove the device", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning())
                    throw new HousemateCommsException("Cannot remove while device is still running");
                remove();
            }
        };
        this.runningValue = BooleanType.createValue(log, listenersFactory, DeviceData.RUNNING_ID, DeviceData.RUNNING_ID, "Whether the device is running or not", false);
        this.startCommand = new RealCommand(log, listenersFactory, DeviceData.START_ID, DeviceData.START_ID, "Start the device", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(!isRunning()) {
                    _start();
                    runningValue.setTypedValues(true);
                }
            }
        };
        this.stopCommand = new RealCommand(log, listenersFactory, DeviceData.STOP_ID, DeviceData.STOP_ID, "Stop the device", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning()) {
                    _stop();
                    runningValue.setTypedValues(false);
                }
            }
        };
        this.errorValue = StringType.createValue(log, listenersFactory, DeviceData.ERROR_ID, DeviceData.ERROR_ID, "Current error for the device", null);
        this.driverProperty = (RealProperty<DeviceDriver.Factory<DRIVER>>) new RealProperty(log, listenersFactory, "driver", "Driver", "The device's driver", driverFactoryType);
        this.driverLoadedValue = BooleanType.createValue(log, listenersFactory, DeviceData.DRIVER_LOADED_ID, DeviceData.DRIVER_LOADED_ID, "Whether the device's driver is loaded or not", false);
        this.commands = new RealList<>(log, listenersFactory, DeviceData.COMMANDS_ID, DeviceData.COMMANDS_ID, COMMANDS_DESCRIPTION);
        this.values = new RealList<>(log, listenersFactory, DeviceData.VALUES_ID, DeviceData.VALUES_ID, VALUES_DESCRIPTION);
        this.properties = new RealList<>(log, listenersFactory, DeviceData.PROPERTIES_ID, DeviceData.PROPERTIES_ID, PROPERTIES_DESCRIPTION);
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
    public RealProperty<DeviceDriver.Factory<DRIVER>> getDriverProperty() {
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
    public final RealList<CommandData, RealCommand> getCommands() {
        return commands;
    }

    @Override
    public final RealList<ValueData, RealValue<?>> getValues() {
        return values;
    }

    @Override
    public final RealList<PropertyData, RealProperty<?>> getProperties() {
        return properties;
    }

    protected final void remove() {
        removedListener.deviceRemoved(this);
    }

    protected final void _start() {
        try {
            // todo, call the driver
        } catch (Throwable t) {
            getErrorValue().setTypedValues("Could not start device: " + t.getMessage());
        }
    }

    protected final void _stop() {
        // todo, call the driver
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

    public interface RemovedListener {
        void deviceRemoved(RealDevice device);
    }

    public interface Factory {
        RealDevice<?> create(DeviceData data, RemovedListener removedListener);
    }
}

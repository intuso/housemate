package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.real.api.internal.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.real.api.internal.impl.type.BooleanType;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.HardwareData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.comms.api.internal.payload.StringPayload;
import com.intuso.housemate.object.api.internal.Hardware;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Base class for all hardwares
 */
public final class RealHardware<DRIVER extends HardwareDriver>
        extends RealObject<HardwareData, HousemateData<?>, RealObject<?, ?, ?, ?>, Hardware.Listener<? super RealHardware<DRIVER>>>
        implements Hardware<
        RealCommand,
        RealCommand,
        RealCommand,
        RealValue<Boolean>,
        RealValue<String>,
        RealProperty<HardwareDriver.Factory<DRIVER>>,
        RealValue<Boolean>,
        RealList<PropertyData, RealProperty<?>>,
        RealHardware<DRIVER>> {

    private final static String PROPERTIES_DESCRIPTION = "The hardware's properties";

    private final RealCommand renameCommand;
    private final RealCommand removeCommand;
    private final RealValue<Boolean> runningValue;
    private final RealCommand startCommand;
    private final RealCommand stopCommand;
    private final RealValue<String> errorValue;
    private final RealProperty<HardwareDriver.Factory<DRIVER>> driverProperty;
    private final RealValue<Boolean> driverLoadedValue;
    private final RealList<PropertyData, RealProperty<?>> properties;

    private final RemovedListener removedListener;

    private DRIVER driver;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data the hardware's data
     */
    @Inject
    public RealHardware(Log log,
                        ListenersFactory listenersFactory,
                        HardwareFactoryType driverFactoryType,
                        @Assisted HardwareData data,
                        @Assisted RemovedListener removedListener) {
        super(log, listenersFactory, data);
        this.removedListener = removedListener;
        this.renameCommand = new RealCommand(log, listenersFactory, HardwareData.RENAME_ID, HardwareData.RENAME_ID, "Rename the hardware", Lists.<RealParameter<?>>newArrayList(StringType.createParameter(log, listenersFactory, HardwareData.NAME_ID, HardwareData.NAME_ID, "The new name"))) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(values != null && values.getChildren().containsKey(HardwareData.NAME_ID)) {
                    String newName = values.getChildren().get(HardwareData.NAME_ID).getFirstValue();
                    if (newName != null && !RealHardware.this.getName().equals(newName)) {
                        RealHardware.this.getData().setName(newName);
                        for(Hardware.Listener<? super RealHardware<DRIVER>> listener : RealHardware.this.getObjectListeners())
                            listener.renamed(RealHardware.this, RealHardware.this.getName(), newName);
                        RealHardware.this.sendMessage(HardwareData.NEW_NAME, new StringPayload(newName));
                    }
                }
            }
        };
        this.removeCommand = new RealCommand(log, listenersFactory, HardwareData.REMOVE_ID, HardwareData.REMOVE_ID, "Remove the hardware", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning())
                    throw new HousemateCommsException("Cannot remove while hardware is still running");
                remove();
            }
        };
        this.runningValue = BooleanType.createValue(log, listenersFactory, HardwareData.RUNNING_ID, HardwareData.RUNNING_ID, "Whether the hardware is running or not", false);
        this.startCommand = new RealCommand(log, listenersFactory, HardwareData.START_ID, HardwareData.START_ID, "Start the hardware", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(!isRunning()) {
                    _start();
                    runningValue.setTypedValues(true);
                }
            }
        };
        this.stopCommand = new RealCommand(log, listenersFactory, HardwareData.STOP_ID, HardwareData.STOP_ID, "Stop the hardware", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning()) {
                    _stop();
                    runningValue.setTypedValues(false);
                }
            }
        };
        this.errorValue = StringType.createValue(log, listenersFactory, HardwareData.ERROR_ID, HardwareData.ERROR_ID, "Current error for the hardware", null);
        this.driverProperty = (RealProperty<HardwareDriver.Factory<DRIVER>>) new RealProperty(log, listenersFactory, "driver", "Driver", "The hardware's driver", driverFactoryType);
        this.driverLoadedValue = BooleanType.createValue(log, listenersFactory, HardwareData.DRIVER_LOADED_ID, HardwareData.DRIVER_LOADED_ID, "Whether the hardware's driver is loaded or not", false);
        this.properties = new RealList<>(log, listenersFactory, HardwareData.PROPERTIES_ID, HardwareData.PROPERTIES_ID, PROPERTIES_DESCRIPTION);
        addChild(renameCommand);
        addChild(removeCommand);
        addChild(runningValue);
        addChild(startCommand);
        addChild(stopCommand);
        addChild(errorValue);
        addChild(driverProperty);
        addChild(driverLoadedValue);
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
    public RealProperty<HardwareDriver.Factory<DRIVER>> getDriverProperty() {
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

    protected final void remove() {
        removedListener.hardwareRemoved(this);
    }

    @Override
    public final RealList<PropertyData, RealProperty<?>> getProperties() {
        return properties;
    }

    protected final void _start() {
        try {
            // todo, call the driver
        } catch (Throwable t) {
            getErrorValue().setTypedValues("Could not start hardware: " + t.getMessage());
        }
    }

    protected final void _stop() {
        // todo, call the driver
    }

    public interface RemovedListener {
        void hardwareRemoved(RealHardware hardware);
    }

    public interface Factory {
        RealHardware<?> create(HardwareData data, RemovedListener removedListener);
    }
}

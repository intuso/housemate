package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.HardwareMapper;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class RealHardwareBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.Hardware.Data, Hardware.Data, Hardware.Listener<? super RealHardwareBridge>>
        implements Hardware<RealCommandBridge,
        RealCommandBridge,
        RealCommandBridge,
        RealValueBridge,
        RealValueBridge,
        RealPropertyBridge,
        RealValueBridge,
        RealListBridge<RealCommandBridge>,
        RealListBridge<RealValueBridge>,
        RealListBridge<RealPropertyBridge>,
        RealListBridge<RealDeviceConnectedBridge>,
        RealHardwareBridge> {

    private final RealCommandBridge renameCommand;
    private final RealCommandBridge removeCommand;
    private final RealValueBridge runningValue;
    private final RealCommandBridge startCommand;
    private final RealCommandBridge stopCommand;
    private final RealValueBridge errorValue;
    private final RealPropertyBridge driverProperty;
    private final RealValueBridge driverLoadedValue;
    private final RealListBridge<RealCommandBridge> commands;
    private final RealListBridge<RealValueBridge> values;
    private final RealListBridge<RealPropertyBridge> properties;
    private final RealListBridge<RealDeviceConnectedBridge> devices;

    @Inject
    protected RealHardwareBridge(@Assisted Logger logger,
                                 HardwareMapper hardwareMapper,
                                 ManagedCollectionFactory managedCollectionFactory,
                                 Receiver.Factory v1_0ReceiverFactory,
                                 com.intuso.housemate.client.messaging.api.internal.Sender.Factory internalSenderFactory,
                                 Factory<RealCommandBridge> commandFactory,
                                 Factory<RealValueBridge> valueFactory,
                                 Factory<RealPropertyBridge> propertyFactory,
                                 Factory<RealListBridge<RealCommandBridge>> commandsFactory,
                                 Factory<RealListBridge<RealValueBridge>> valuesFactory,
                                 Factory<RealListBridge<RealPropertyBridge>> propertiesFactory,
                                 Factory<RealListBridge<RealDeviceConnectedBridge>> devicesFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Hardware.Data.class, hardwareMapper, managedCollectionFactory, v1_0ReceiverFactory, internalSenderFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        runningValue = valueFactory.create(ChildUtil.logger(logger, Runnable.RUNNING_ID));
        startCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.START_ID));
        stopCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.STOP_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID));
        commands = commandsFactory.create(ChildUtil.logger(logger, Hardware.COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, Hardware.VALUES_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, Hardware.PROPERTIES_ID));
        devices = devicesFactory.create(ChildUtil.logger(logger, Hardware.DEVICES_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        renameCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID)
        );
        removeCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Removeable.REMOVE_ID),
                ChildUtil.name(internalName, Removeable.REMOVE_ID)
        );
        runningValue.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.RUNNING_ID),
                ChildUtil.name(internalName, com.intuso.housemate.client.api.internal.Runnable.RUNNING_ID)
        );
        startCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.START_ID),
                ChildUtil.name(internalName, Runnable.START_ID)
        );
        stopCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.STOP_ID),
                ChildUtil.name(internalName, Runnable.STOP_ID)
        );
        errorValue.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Failable.ERROR_ID),
                ChildUtil.name(internalName, Failable.ERROR_ID)
        );
        driverProperty.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_ID)
        );
        driverLoadedValue.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_LOADED_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_LOADED_ID)
        );
        commands.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Hardware.COMMANDS_ID),
                ChildUtil.name(internalName, Hardware.COMMANDS_ID)
        );
        values.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Hardware.VALUES_ID),
                ChildUtil.name(internalName, Hardware.VALUES_ID)
        );
        properties.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Hardware.PROPERTIES_ID),
                ChildUtil.name(internalName, Hardware.PROPERTIES_ID)
        );
        devices.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Hardware.DEVICES_ID),
                ChildUtil.name(internalName, Hardware.DEVICES_ID)
        );
    }

    @Override
    protected void uninitChildren() {
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

    @Override
    public RealCommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public RealCommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealValueBridge getRunningValue() {
        return runningValue;
    }

    @Override
    public RealCommandBridge getStartCommand() {
        return startCommand;
    }

    @Override
    public RealCommandBridge getStopCommand() {
        return stopCommand;
    }

    @Override
    public RealValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public RealPropertyBridge getDriverProperty() {
        return driverProperty;
    }

    @Override
    public RealValueBridge getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public RealListBridge<RealCommandBridge> getCommands() {
        return commands;
    }

    @Override
    public RealListBridge<RealValueBridge> getValues() {
        return values;
    }

    @Override
    public RealListBridge<RealPropertyBridge> getProperties() {
        return properties;
    }

    @Override
    public RealListBridge<RealDeviceConnectedBridge> getDeviceConnecteds() {
        return devices;
    }

    @Override
    public RealObjectBridge<?, ?, ?> getChild(String id) {
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
}

package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.HardwareMapper;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyHardwareBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Hardware.Data, Hardware.Data, Hardware.Listener<? super ProxyHardwareBridge>>
        implements Hardware<ProxyCommandBridge,
        ProxyCommandBridge,
        ProxyCommandBridge,
        ProxyValueBridge,
        ProxyValueBridge,
        ProxyPropertyBridge,
        ProxyValueBridge,
        ProxyListBridge<ProxyCommandBridge>,
        ProxyListBridge<ProxyValueBridge>,
        ProxyListBridge<ProxyPropertyBridge>,
        ProxyListBridge<ProxyDeviceBridge>,
        ProxyHardwareBridge> {

    private final ProxyCommandBridge renameCommand;
    private final ProxyCommandBridge removeCommand;
    private final ProxyValueBridge runningValue;
    private final ProxyCommandBridge startCommand;
    private final ProxyCommandBridge stopCommand;
    private final ProxyValueBridge errorValue;
    private final ProxyPropertyBridge driverProperty;
    private final ProxyValueBridge driverLoadedValue;
    private final ProxyListBridge<ProxyCommandBridge> commands;
    private final ProxyListBridge<ProxyValueBridge> values;
    private final ProxyListBridge<ProxyPropertyBridge> properties;
    private final ProxyListBridge<ProxyDeviceBridge> devices;

    @Inject
    protected ProxyHardwareBridge(@Assisted Logger logger,
                                  HardwareMapper hardwareMapper,
                                  ManagedCollectionFactory managedCollectionFactory,
                                  com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                  Sender.Factory v1_0SenderFactory,
                                  Factory<ProxyCommandBridge> commandFactory,
                                  Factory<ProxyListBridge<ProxyCommandBridge>> commandsFactory,
                                  Factory<ProxyValueBridge> valueFactory,
                                  Factory<ProxyListBridge<ProxyValueBridge>> valuesFactory,
                                  Factory<ProxyPropertyBridge> propertyFactory,
                                  Factory<ProxyListBridge<ProxyPropertyBridge>> propertiesFactory,
                                  Factory<ProxyListBridge<ProxyDeviceBridge>> devicesFactory) {
        super(logger, Hardware.Data.class, hardwareMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
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
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID)
        );
        removeCommand.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Removeable.REMOVE_ID),
                ChildUtil.name(internalName, Removeable.REMOVE_ID)
        );
        runningValue.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.RUNNING_ID),
                ChildUtil.name(internalName, Runnable.RUNNING_ID)
        );
        startCommand.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.START_ID),
                ChildUtil.name(internalName, Runnable.START_ID)
        );
        stopCommand.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.STOP_ID),
                ChildUtil.name(internalName, Runnable.STOP_ID)
        );
        errorValue.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Failable.ERROR_ID),
                ChildUtil.name(internalName, Failable.ERROR_ID)
        );
        driverProperty.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_ID)
        );
        driverLoadedValue.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_LOADED_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_LOADED_ID)
        );
        commands.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Hardware.COMMANDS_ID),
                ChildUtil.name(internalName, Hardware.COMMANDS_ID)
        );
        values.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Hardware.VALUES_ID),
                ChildUtil.name(internalName, Hardware.VALUES_ID)
        );
        properties.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Hardware.PROPERTIES_ID),
                ChildUtil.name(internalName, Hardware.PROPERTIES_ID)
        );
        devices.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Hardware.DEVICES_ID),
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
    public ProxyCommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public ProxyCommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ProxyValueBridge getRunningValue() {
        return runningValue;
    }

    @Override
    public ProxyCommandBridge getStartCommand() {
        return startCommand;
    }

    @Override
    public ProxyCommandBridge getStopCommand() {
        return stopCommand;
    }

    @Override
    public ProxyValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public ProxyPropertyBridge getDriverProperty() {
        return driverProperty;
    }

    @Override
    public ProxyValueBridge getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public ProxyListBridge<ProxyCommandBridge> getCommands() {
        return commands;
    }

    @Override
    public ProxyListBridge<ProxyValueBridge> getValues() {
        return values;
    }

    @Override
    public ProxyListBridge<ProxyPropertyBridge> getProperties() {
        return properties;
    }

    @Override
    public ProxyListBridge<ProxyDeviceBridge> getDevices() {
        return devices;
    }
}
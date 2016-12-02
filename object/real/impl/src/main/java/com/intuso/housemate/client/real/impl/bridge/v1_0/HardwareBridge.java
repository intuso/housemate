package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.HardwareMapper;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class HardwareBridge
        extends BridgeObject<com.intuso.housemate.client.v1_0.api.object.Hardware.Data, Hardware.Data, Hardware.Listener<? super HardwareBridge>>
        implements Hardware<CommandBridge, CommandBridge, CommandBridge, ValueBridge, ValueBridge, PropertyBridge, ValueBridge, ListBridge<PropertyBridge>, HardwareBridge> {

    private final CommandBridge renameCommand;
    private final CommandBridge removeCommand;
    private final ValueBridge runningValue;
    private final CommandBridge startCommand;
    private final CommandBridge stopCommand;
    private final ValueBridge errorValue;
    private final PropertyBridge driverProperty;
    private final ValueBridge driverLoadedValue;
    private final ListBridge<PropertyBridge> properties;

    @Inject
    protected HardwareBridge(@Assisted Logger logger,
                             HardwareMapper hardwareMapper,
                             BridgeObject.Factory<CommandBridge> commandFactory,
                             BridgeObject.Factory<ValueBridge> valueFactory,
                             BridgeObject.Factory<PropertyBridge> propertyFactory,
                             BridgeObject.Factory<ListBridge<PropertyBridge>> propertiesFactory,
                             ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Hardware.Data.class, hardwareMapper, listenersFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        runningValue = valueFactory.create(ChildUtil.logger(logger, Runnable.RUNNING_ID));
        startCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.START_ID));
        stopCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.STOP_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, Hardware.PROPERTIES_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        renameCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(internalName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID),
                connection);
        removeCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(internalName, com.intuso.housemate.client.v1_0.api.Removeable.REMOVE_ID),
                ChildUtil.name(internalName, Removeable.REMOVE_ID),
                connection);
        runningValue.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(internalName, com.intuso.housemate.client.v1_0.api.Runnable.RUNNING_ID),
                ChildUtil.name(internalName, com.intuso.housemate.client.api.internal.Runnable.RUNNING_ID),
                connection);
        startCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(internalName, com.intuso.housemate.client.v1_0.api.Runnable.START_ID),
                ChildUtil.name(internalName, Runnable.START_ID),
                connection);
        stopCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(internalName, com.intuso.housemate.client.v1_0.api.Runnable.STOP_ID),
                ChildUtil.name(internalName, Runnable.STOP_ID),
                connection);
        errorValue.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(internalName, com.intuso.housemate.client.v1_0.api.Failable.ERROR_ID),
                ChildUtil.name(internalName, Failable.ERROR_ID),
                connection);
        driverProperty.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(internalName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_ID),
                connection);
        driverLoadedValue.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(internalName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_LOADED_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_LOADED_ID),
                connection);
        properties.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(internalName, com.intuso.housemate.client.v1_0.api.object.Hardware.PROPERTIES_ID),
                ChildUtil.name(internalName, Hardware.PROPERTIES_ID),
                connection);
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
        properties.uninit();
    }

    @Override
    public CommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ValueBridge getRunningValue() {
        return runningValue;
    }

    @Override
    public CommandBridge getStartCommand() {
        return startCommand;
    }

    @Override
    public CommandBridge getStopCommand() {
        return stopCommand;
    }

    @Override
    public ValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public PropertyBridge getDriverProperty() {
        return driverProperty;
    }

    @Override
    public ValueBridge getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public ListBridge<PropertyBridge> getProperties() {
        return properties;
    }
}

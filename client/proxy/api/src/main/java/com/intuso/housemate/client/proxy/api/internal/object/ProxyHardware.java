package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.proxy.api.internal.*;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/*
 * @param <PROPERTIES> the type of the properties list
 * @param <HARDWARE> the type of the hardware
 */
public abstract class ProxyHardware<
        COMMAND extends ProxyCommand<?, ?, COMMAND>,
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUE extends ProxyValue<?, VALUE>,
        VALUES extends ProxyList<? extends ProxyValue<?, ?>, ?>,
        PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
        PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
        DEVICES extends ProxyList<? extends ProxyConnectedDevice<?, ?, ?, ?, ?>, ?>,
        HARDWARE extends ProxyHardware<COMMAND, COMMANDS, VALUE, VALUES, PROPERTY, PROPERTIES, DEVICES, HARDWARE>>
        extends ProxyObject<Hardware.Data, Hardware.Listener<? super HARDWARE>>
        implements Hardware<COMMAND, COMMAND, COMMAND, VALUE, VALUE, PROPERTY, VALUE, COMMANDS, VALUES, PROPERTIES, DEVICES, HARDWARE>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND>,
        ProxyRenameable<COMMAND>,
        ProxyRunnable<COMMAND, VALUE>,
        ProxyUsesDriver<PROPERTY, VALUE> {

    private final COMMAND renameCommand;
    private final COMMAND removeCommand;
    private final VALUE runningValue;
    private final COMMAND startCommand;
    private final COMMAND stopCommand;
    private final VALUE errorValue;
    private final PROPERTY driverProperty;
    private final VALUE driverLoadedValue;
    private final COMMANDS commands;
    private final VALUES values;
    private final PROPERTIES properties;
    private final DEVICES devices;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyHardware(Logger logger,
                         ManagedCollectionFactory managedCollectionFactory,
                         ProxyObject.Factory<COMMAND> commandFactory,
                         ProxyObject.Factory<COMMANDS> commandsFactory,
                         ProxyObject.Factory<VALUE> valueFactory,
                         ProxyObject.Factory<VALUES> valuesFactory,
                         ProxyObject.Factory<PROPERTY> propertyFactory,
                         ProxyObject.Factory<PROPERTIES> propertiesFactory,
                         ProxyObject.Factory<DEVICES> devicesFactory) {
        super(logger, Hardware.Data.class, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID));
        runningValue = valueFactory.create(ChildUtil.logger(logger, RUNNING_ID));
        startCommand = commandFactory.create(ChildUtil.logger(logger, START_ID));
        stopCommand = commandFactory.create(ChildUtil.logger(logger, STOP_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID));
        commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID));
        devices = devicesFactory.create(ChildUtil.logger(logger, DEVICES_ID));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, RENAME_ID), connection);
        removeCommand.init(ChildUtil.name(name, REMOVE_ID), connection);
        runningValue.init(ChildUtil.name(name, RUNNING_ID), connection);
        startCommand.init(ChildUtil.name(name, START_ID), connection);
        stopCommand.init(ChildUtil.name(name, STOP_ID), connection);
        errorValue.init(ChildUtil.name(name, ERROR_ID), connection);
        driverProperty.init(ChildUtil.name(name, DRIVER_ID), connection);
        driverLoadedValue.init(ChildUtil.name(name, DRIVER_LOADED_ID), connection);
        commands.init(ChildUtil.name(name, COMMANDS_ID), connection);
        values.init(ChildUtil.name(name, VALUES_ID), connection);
        properties.init(ChildUtil.name(name, PROPERTIES_ID), connection);
        devices.init(ChildUtil.name(name, DEVICES_ID), connection);
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
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public final boolean isRunning() {
        return runningValue.getValue() != null
                && runningValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(runningValue.getValue().getFirstValue());
    }

    @Override
    public VALUE getRunningValue() {
        return runningValue;
    }

    @Override
    public COMMAND getStartCommand() {
        return startCommand;
    }

    @Override
    public COMMAND getStopCommand() {
        return stopCommand;
    }

    @Override
    public final String getError() {
        return errorValue.getValue() != null ? errorValue.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return errorValue;
    }

    @Override
    public PROPERTY getDriverProperty() {
        return driverProperty;
    }

    @Override
    public final boolean isDriverLoaded() {
        return driverLoadedValue.getValue() != null
                && driverLoadedValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(driverLoadedValue.getValue().getFirstValue());
    }

    @Override
    public VALUE getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public COMMANDS getCommands() {
        return commands;
    }

    @Override
    public VALUES getValues() {
        return values;
    }

    @Override
    public final PROPERTIES getProperties() {
        return properties;
    }

    @Override
    public final DEVICES getConnectedDevices() {
        return devices;
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
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
        return null;
    }
}

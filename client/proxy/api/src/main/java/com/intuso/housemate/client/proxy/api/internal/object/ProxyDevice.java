package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.proxy.api.internal.*;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * @param <COMMAND> the type of the commands
 * @param <VALUE> the type of the values
 * @param <PROPERTY> the type of the properties
 * @param <DEVICE> the type of the device
 */
public abstract class ProxyDevice<
        COMMAND extends ProxyCommand<?, ?, COMMAND>,
        VALUE extends ProxyValue<?, VALUE>,
        PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
        PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
        FEATURES extends ProxyList<? extends ProxyFeature<?, ?, ?>, ?>,
        DEVICE extends ProxyDevice<COMMAND, VALUE, PROPERTY, PROPERTIES, FEATURES, DEVICE>>
        extends ProxyObject<Device.Data, Device.Listener<? super DEVICE>>
        implements Device<COMMAND, COMMAND, COMMAND, VALUE, VALUE, PROPERTY, VALUE, PROPERTIES, FEATURES, DEVICE>,
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
    private final PROPERTIES properties;
    private final FEATURES features;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDevice(Logger logger,
                       ListenersFactory listenersFactory,
                       ProxyObject.Factory<COMMAND> commandFactory,
                       ProxyObject.Factory<VALUE> valueFactory,
                       ProxyObject.Factory<PROPERTY> propertyFactory,
                       ProxyObject.Factory<PROPERTIES> propertiesFactory,
                       ProxyObject.Factory<FEATURES> featuresFactory) {
        super(logger, Device.Data.class, listenersFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        runningValue = valueFactory.create(ChildUtil.logger(logger, com.intuso.housemate.client.v1_0.api.Runnable.RUNNING_ID));
        startCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.START_ID));
        stopCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.STOP_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, Device.PROPERTIES_ID));
        features = featuresFactory.create(ChildUtil.logger(logger, Device.FEATURES_ID));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), connection);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), connection);
        runningValue.init(ChildUtil.name(name, Runnable.RUNNING_ID), connection);
        startCommand.init(ChildUtil.name(name, Runnable.START_ID), connection);
        stopCommand.init(ChildUtil.name(name, Runnable.STOP_ID), connection);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), connection);
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), connection);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), connection);
        properties.init(ChildUtil.name(name, Device.PROPERTIES_ID), connection);
        features.init(ChildUtil.name(name, Device.FEATURES_ID), connection);
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
        features.uninit();
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
    public final PROPERTIES getProperties() {
        return properties;
    }

    @Override
    public final FEATURES getFeatures() {
        return features;
    }
}

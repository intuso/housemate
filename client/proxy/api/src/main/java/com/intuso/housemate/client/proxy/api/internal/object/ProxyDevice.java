package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.proxy.api.internal.*;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * @param <COMMAND> the type of the commands
 * @param <VALUE> the type of the values
 * @param <DEVICE> the type of the device
 */
public abstract class ProxyDevice<
        COMMAND extends ProxyCommand<?, ?, COMMAND>,
        VALUE extends ProxyValue<?, VALUE>,
        FEATURES extends ProxyList<? extends ProxyFeature<?, ?, ?, ?, ?, ?, ?>, ?>,
        DEVICE extends ProxyDevice<COMMAND, VALUE, FEATURES, DEVICE>>
        extends ProxyObject<Device.Data, Device.Listener<? super DEVICE>>
        implements Device<COMMAND, VALUE, VALUE, FEATURES, DEVICE>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND>,
        ProxyRenameable<COMMAND>,
        ProxyRunnable<COMMAND, VALUE> {

    private final COMMAND renameCommand;
    private final COMMAND removeCommand;
    private final VALUE runningValue;
    private final COMMAND startCommand;
    private final COMMAND stopCommand;
    private final VALUE errorValue;
    private final FEATURES features;
    private final COMMAND addFeatureCommand;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDevice(Logger logger,
                       ManagedCollectionFactory managedCollectionFactory,
                       ProxyObject.Factory<COMMAND> commandFactory,
                       ProxyObject.Factory<VALUE> valueFactory,
                       ProxyObject.Factory<FEATURES> featuresFactory) {
        super(logger, Device.Data.class, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID));
        runningValue = valueFactory.create(ChildUtil.logger(logger, RUNNING_ID));
        startCommand = commandFactory.create(ChildUtil.logger(logger, START_ID));
        stopCommand = commandFactory.create(ChildUtil.logger(logger, STOP_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID));
        features = featuresFactory.create(ChildUtil.logger(logger, FEATURES_ID));
        addFeatureCommand = commandFactory.create(ChildUtil.logger(logger, ADD_FEATURE_ID));
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
        features.init(ChildUtil.name(name, FEATURES_ID), connection);
        addFeatureCommand.init(ChildUtil.name(name, ADD_FEATURE_ID), connection);
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
        features.uninit();
        addFeatureCommand.uninit();
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
    public final FEATURES getFeatures() {
        return features;
    }

    @Override
    public COMMAND getAddFeatureCommand() {
        return addFeatureCommand;
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
        else if(FEATURES_ID.equals(id))
            return features;
        else if(ADD_FEATURE_ID.equals(id))
            return addFeatureCommand;
        return null;
    }
}

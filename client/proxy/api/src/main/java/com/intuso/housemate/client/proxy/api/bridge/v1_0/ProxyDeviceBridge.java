package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.DeviceMapper;
import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyDeviceBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Device.Data, Device.Data, Device.Listener<? super ProxyDeviceBridge>>
        implements Device<
        ProxyCommandBridge,
        ProxyValueBridge,
        ProxyValueBridge,
        ProxyListBridge<ProxyFeatureBridge>,
        ProxyDeviceBridge> {

    private final ProxyCommandBridge renameCommand;
    private final ProxyCommandBridge removeCommand;
    private final ProxyValueBridge runningValue;
    private final ProxyCommandBridge startCommand;
    private final ProxyCommandBridge stopCommand;
    private final ProxyValueBridge errorValue;
    private final ProxyListBridge<ProxyFeatureBridge> features;
    private final ProxyCommandBridge addFeatureCommand;

    @Inject
    protected ProxyDeviceBridge(@Assisted Logger logger,
                                DeviceMapper deviceMapper,
                                Factory<ProxyCommandBridge> commandFactory,
                                Factory<ProxyValueBridge> valueFactory,
                                Factory<ProxyPropertyBridge> propertyFactory,
                                Factory<ProxyListBridge<ProxyPropertyBridge>> propertiesFactory,
                                Factory<ProxyListBridge<ProxyFeatureBridge>> featuresFactory,
                                ManagedCollectionFactory managedCollectionFactory) {
        super(logger, Device.Data.class, deviceMapper, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        runningValue = valueFactory.create(ChildUtil.logger(logger, Runnable.RUNNING_ID));
        startCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.START_ID));
        stopCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.STOP_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        features = featuresFactory.create(ChildUtil.logger(logger, Device.FEATURES_ID));
        addFeatureCommand = commandFactory.create(ChildUtil.logger(logger, Device.ADD_FEATURE_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        renameCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID),
                connection);
        removeCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Removeable.REMOVE_ID),
                ChildUtil.name(internalName, Removeable.REMOVE_ID),
                connection);
        runningValue.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.RUNNING_ID),
                ChildUtil.name(internalName, Runnable.RUNNING_ID),
                connection);
        startCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.START_ID),
                ChildUtil.name(internalName, Runnable.START_ID),
                connection);
        stopCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.STOP_ID),
                ChildUtil.name(internalName, Runnable.STOP_ID),
                connection);
        errorValue.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Failable.ERROR_ID),
                ChildUtil.name(internalName, Failable.ERROR_ID),
                connection);
        features.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Device.FEATURES_ID),
                ChildUtil.name(internalName, Device.FEATURES_ID),
                connection);
        addFeatureCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Device.ADD_FEATURE_ID),
                ChildUtil.name(internalName, Device.ADD_FEATURE_ID),
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
        features.uninit();
        addFeatureCommand.uninit();
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
    public ProxyListBridge<ProxyFeatureBridge> getFeatures() {
        return features;
    }

    @Override
    public ProxyCommandBridge getAddFeatureCommand() {
        return addFeatureCommand;
    }
}

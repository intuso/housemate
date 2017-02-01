package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.FeatureMapper;
import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.UsesDriver;
import com.intuso.housemate.client.api.internal.object.Feature;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyFeatureBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Feature.Data, Feature.Data, Feature.Listener<? super ProxyFeatureBridge>>
        implements Feature<ProxyCommandBridge,
        ProxyCommandBridge,
        ProxyValueBridge,
        ProxyPropertyBridge,
        ProxyValueBridge,
        ProxyListBridge<ProxyCommandBridge>,
        ProxyListBridge<ProxyValueBridge>,
        ProxyListBridge<ProxyPropertyBridge>,
        ProxyFeatureBridge> {

    private final ProxyCommandBridge renameCommand;
    private final ProxyCommandBridge removeCommand;
    private final ProxyValueBridge errorValue;
    private final ProxyPropertyBridge driverProperty;
    private final ProxyValueBridge driverLoadedValue;
    private final ProxyListBridge<ProxyCommandBridge> commands;
    private final ProxyListBridge<ProxyValueBridge> values;
    private final ProxyListBridge<ProxyPropertyBridge> properties;

    @Inject
    protected ProxyFeatureBridge(@Assisted Logger logger,
                                 FeatureMapper featureMapper,
                                 Factory<ProxyCommandBridge> commandFactory,
                                 Factory<ProxyValueBridge> valueFactory,
                                 Factory<ProxyPropertyBridge> propertyFactory,
                                 Factory<ProxyListBridge<ProxyCommandBridge>> commandsFactory,
                                 Factory<ProxyListBridge<ProxyValueBridge>> valuesFactory,
                                 Factory<ProxyListBridge<ProxyPropertyBridge>> propertiesFactory,
                                 ManagedCollectionFactory managedCollectionFactory) {
        super(logger, Feature.Data.class, featureMapper, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID));
        commands = commandsFactory.create(ChildUtil.logger(logger, Feature.COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, Feature.VALUES_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, Feature.PROPERTIES_ID));
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
        errorValue.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Failable.ERROR_ID),
                ChildUtil.name(internalName, Failable.ERROR_ID),
                connection);
        driverProperty.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_ID),
                connection);
        driverLoadedValue.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_LOADED_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_LOADED_ID),
                connection);
        commands.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Feature.COMMANDS_ID),
                ChildUtil.name(internalName, Feature.COMMANDS_ID),
                connection);
        values.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Feature.VALUES_ID),
                ChildUtil.name(internalName, Feature.VALUES_ID),
                connection);
        properties.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Feature.PROPERTIES_ID),
                ChildUtil.name(internalName, Feature.PROPERTIES_ID),
                connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        errorValue.uninit();
        driverProperty.uninit();
        driverLoadedValue.uninit();
        commands.uninit();
        values.uninit();
        properties.uninit();
    }@Override
    public ProxyCommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public ProxyCommandBridge getRemoveCommand() {
        return removeCommand;
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
}

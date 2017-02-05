package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.DeviceMapper;
import com.intuso.housemate.client.api.internal.Renameable;
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
        implements Device<ProxyCommandBridge,
                ProxyListBridge<ProxyCommandBridge>,
                ProxyListBridge<ProxyValueBridge>,
                ProxyListBridge<ProxyPropertyBridge>,
        ProxyDeviceBridge> {

    private final ProxyCommandBridge renameCommand;
    private final ProxyListBridge<ProxyCommandBridge> commands;
    private final ProxyListBridge<ProxyValueBridge> values;
    private final ProxyListBridge<ProxyPropertyBridge> properties;

    @Inject
    protected ProxyDeviceBridge(@Assisted Logger logger,
                                DeviceMapper featureMapper,
                                Factory<ProxyCommandBridge> commandFactory,
                                Factory<ProxyListBridge<ProxyCommandBridge>> commandsFactory,
                                Factory<ProxyListBridge<ProxyValueBridge>> valuesFactory,
                                Factory<ProxyListBridge<ProxyPropertyBridge>> propertiesFactory,
                                ManagedCollectionFactory managedCollectionFactory) {
        super(logger, Device.Data.class, featureMapper, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        commands = commandsFactory.create(ChildUtil.logger(logger, Device.COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, Device.VALUES_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, Device.PROPERTIES_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        renameCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID),
                connection);
        commands.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Device.COMMANDS_ID),
                ChildUtil.name(internalName, Device.COMMANDS_ID),
                connection);
        values.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Device.VALUES_ID),
                ChildUtil.name(internalName, Device.VALUES_ID),
                connection);
        properties.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Device.PROPERTIES_ID),
                ChildUtil.name(internalName, Device.PROPERTIES_ID),
                connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        commands.uninit();
        values.uninit();
        properties.uninit();
    }

    @Override
    public ProxyCommandBridge getRenameCommand() {
        return renameCommand;
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

package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.DeviceComponentMapper;
import com.intuso.housemate.client.api.internal.object.DeviceComponent;
import com.intuso.housemate.client.api.internal.object.view.DeviceComponentView;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Set;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyDeviceComponentBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.DeviceComponent.Data, DeviceComponent.Data, DeviceComponent.Listener<? super ProxyDeviceComponentBridge>, DeviceComponentView>
        implements DeviceComponent<
            ProxyListBridge<ProxyCommandBridge>,
            ProxyListBridge<ProxyValueBridge>,
            ProxyDeviceComponentBridge> {

    private final ProxyListBridge<ProxyCommandBridge> commands;
    private final ProxyListBridge<ProxyValueBridge> values;

    @Inject
    protected ProxyDeviceComponentBridge(@Assisted Logger logger,
                                         DeviceComponentMapper deviceComponentMapper,
                                         ManagedCollectionFactory managedCollectionFactory,
                                         com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                         Sender.Factory v1_0SenderFactory,
                                         Factory<ProxyCommandBridge> commandFactory,
                                         Factory<ProxyListBridge<ProxyCommandBridge>> commandsFactory,
                                         Factory<ProxyValueBridge> valueFactory,
                                         Factory<ProxyListBridge<ProxyValueBridge>> valuesFactory,
                                         Factory<ProxyPropertyBridge> propertyFactory,
                                         Factory<ProxyListBridge<ProxyPropertyBridge>> propertiesFactory,
                                         Factory<ProxyListBridge<ProxyDeviceConnectedBridge>> devicesFactory) {
        super(logger, DeviceComponent.Data.class, deviceComponentMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
        commands = commandsFactory.create(ChildUtil.logger(logger, DeviceComponent.COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, DeviceComponent.VALUES_ID));
    }

    @Override
    public Set<String> getClasses() {
        return getData().getClasses();
    }

    @Override
    public Set<String> getAbilities() {
        return getData().getAbilities();
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        commands.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.DeviceComponent.COMMANDS_ID),
                ChildUtil.name(internalName, DeviceComponent.COMMANDS_ID)
        );
        values.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.DeviceComponent.VALUES_ID),
                ChildUtil.name(internalName, DeviceComponent.VALUES_ID)
        );
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        commands.uninit();
        values.uninit();
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
    public ProxyObjectBridge<?, ?, ?, ?> getChild(String id) {
        if(COMMANDS_ID.equals(id))
            return commands;
        else if(VALUES_ID.equals(id))
            return values;
        return null;
    }
}

package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.ObjectMapper;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.view.DeviceView;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Set;

/**
 * Created by tomc on 28/11/16.
 */
public abstract class ProxyDeviceBridge<VERSION_DATA extends com.intuso.housemate.client.v1_0.api.object.Device.Data,
        INTERNAL_DATA extends Device.Data,
        LISTENER extends Device.Listener<? super DEVICE>,
        VIEW extends DeviceView<?>,
        DEVICE extends ProxyDeviceBridge<VERSION_DATA, INTERNAL_DATA, LISTENER, VIEW, DEVICE>>
        extends ProxyObjectBridge<VERSION_DATA, INTERNAL_DATA, LISTENER, VIEW>
        implements Device<INTERNAL_DATA,
        LISTENER,
        ProxyCommandBridge,
        ProxyListBridge<ProxyDeviceComponentBridge>,
        VIEW,
        DEVICE> {

    private final ProxyCommandBridge renameCommand;
    private final ProxyListBridge<ProxyDeviceComponentBridge> components;

    @Inject
    protected ProxyDeviceBridge(@Assisted Logger logger,
                                Class<INTERNAL_DATA> internalDataClass,
                                ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper,
                                ManagedCollectionFactory managedCollectionFactory,
                                com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                Sender.Factory v1_0SenderFactory,
                                Factory<ProxyCommandBridge> commandFactory,
                                Factory<ProxyListBridge<ProxyDeviceComponentBridge>> componentsFactory) {
        super(logger, internalDataClass, dataMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        components = componentsFactory.create(ChildUtil.logger(logger, Device.DEVICE_COMPONENTS_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        renameCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID)
        );
        components.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Device.DEVICE_COMPONENTS_ID),
                ChildUtil.name(internalName, Device.DEVICE_COMPONENTS_ID)
        );
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        components.uninit();
    }

    @Override
    public ProxyCommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public ProxyListBridge<ProxyDeviceComponentBridge> getDeviceComponents() {
        return components;
    }

    @Override
    public ProxyObjectBridge<?, ?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(DEVICE_COMPONENTS_ID.equals(id))
            return components;
        return null;
    }
}

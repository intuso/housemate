package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.DeviceConnectedMapper;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.view.DeviceConnectedView;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Set;

/**
 * Created by tomc on 28/11/16.
 */
public class RealDeviceConnectedBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.Device.Connected.Data, Device.Connected.Data, Device.Connected.Listener<? super RealDeviceConnectedBridge>, DeviceConnectedView>
        implements Device.Connected<RealCommandBridge,
                RealListBridge<RealDeviceComponentBridge>,
        RealDeviceConnectedBridge> {

    private final RealCommandBridge renameCommand;
    private final RealListBridge<RealDeviceComponentBridge> components;

    @Inject
    protected RealDeviceConnectedBridge(@Assisted Logger logger,
                                        DeviceConnectedMapper deviceConnectedMapper,
                                        ManagedCollectionFactory managedCollectionFactory,
                                        Factory<RealCommandBridge> commandFactory,
                                        Factory<RealListBridge<RealDeviceComponentBridge>> componentsFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Device.Connected.Data.class, deviceConnectedMapper, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        components = componentsFactory.create(ChildUtil.logger(logger, Device.DEVICE_COMPONENTS_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Sender.Factory internalSenderFactory, com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory, com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0SenderFactory, Receiver.Factory v1_0ReceiverFactory) {
        super.initChildren(versionName, internalName, internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
        renameCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID),
                internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
        components.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Device.DEVICE_COMPONENTS_ID),
                ChildUtil.name(internalName, Device.DEVICE_COMPONENTS_ID),
                internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        components.uninit();
    }

    @Override
    public RealCommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public RealListBridge<RealDeviceComponentBridge> getDeviceComponents() {
        return components;
    }

    @Override
    public RealObjectBridge<?, ?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(DEVICE_COMPONENTS_ID.equals(id))
            return components;
        return null;
    }
}

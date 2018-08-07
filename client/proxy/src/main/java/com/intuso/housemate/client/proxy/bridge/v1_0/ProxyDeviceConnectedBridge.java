package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.DeviceConnectedMapper;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.view.DeviceConnectedView;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyDeviceConnectedBridge
        extends ProxyDeviceBridge<com.intuso.housemate.client.v1_0.api.object.Device.Connected.Data,
        Device.Connected.Data,
        Device.Connected.Listener<? super ProxyDeviceConnectedBridge>,
        DeviceConnectedView,
        ProxyDeviceConnectedBridge>
        implements Device.Connected<ProxyCommandBridge,
        ProxyListBridge<ProxyDeviceComponentBridge>,
        ProxyDeviceConnectedBridge> {

    @Inject
    protected ProxyDeviceConnectedBridge(@Assisted Logger logger,
                                         DeviceConnectedMapper dataMapper,
                                         ManagedCollectionFactory managedCollectionFactory,
                                         com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                         Sender.Factory v1_0SenderFactory,
                                         Factory<ProxyCommandBridge> commandFactory,
                                         Factory<ProxyListBridge<ProxyDeviceComponentBridge>> componentsFactory) {
        super(logger, Device.Connected.Data.class, dataMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory, commandFactory, componentsFactory);
    }
}

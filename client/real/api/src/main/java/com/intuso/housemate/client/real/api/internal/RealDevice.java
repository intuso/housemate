package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.proxy.internal.simple.SimpleProxyConnectedDevice;

/**
 * Base class for all devices
 */
public interface RealDevice<
        STRING_VALUE extends RealValue<String, ?, ?>,
        COMMAND extends RealCommand<?, ?, ?>,
        DEVICES extends RealList<? extends RealProperty<ObjectReference<SimpleProxyConnectedDevice>, ?, ?, ?>, ?>,
        DEVICE extends RealDevice<STRING_VALUE, COMMAND, DEVICES, DEVICE>>
        extends Device<STRING_VALUE, COMMAND, DEVICES, DEVICE> {

    interface Container<DEVICE extends RealDevice<?, ?, ?, ?>, DEVICES extends RealList<? extends DEVICE, ?>> extends Device.Container<DEVICES>, RemoveCallback<DEVICE> {
        void addDevice(DEVICE device);
    }

    interface RemoveCallback<DEVICE extends RealDevice<?, ?, ?, ?>> {
        void removeDevice(DEVICE device);
    }
}

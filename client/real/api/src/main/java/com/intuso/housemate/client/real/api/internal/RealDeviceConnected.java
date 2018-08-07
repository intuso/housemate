package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.view.DeviceConnectedView;

/**
 * Base class for all devices
 */
public interface RealDeviceConnected<
        COMMAND extends RealCommand<?, ?, ?>,
        DEVICE_COMPONENTS extends RealList<? extends RealDeviceComponent<?, ?, ?>, ?>,
        DEVICE extends RealDeviceConnected<COMMAND, DEVICE_COMPONENTS, DEVICE>>
        extends RealDevice<Device.Connected.Data, Device.Connected.Listener<? super DEVICE>, COMMAND, DEVICE_COMPONENTS, DeviceConnectedView, DEVICE>,
        Device.Connected<COMMAND, DEVICE_COMPONENTS, DEVICE> {

    interface Container<DEVICE extends RealDeviceConnected<?, ?, ?>, DEVICES extends RealList<? extends DEVICE, ?>> extends Device.Connected.Container<DEVICES> {
        void addConnectedDevice(DEVICE device);
    }
}

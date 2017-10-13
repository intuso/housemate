package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.view.DeviceConnectedView;

/**
 * Base class for all devices
 */
public interface RealDeviceConnected<
        COMMAND extends RealCommand<?, ?, ?>,
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        DEVICE extends RealDeviceConnected<COMMAND, COMMANDS, VALUES, DEVICE>>
        extends RealDevice<Device.Connected.Data, Device.Connected.Listener<? super DEVICE>, COMMAND, COMMANDS, VALUES, DeviceConnectedView, DEVICE>,
            Device.Connected<COMMAND, COMMANDS, VALUES, DEVICE> {

    interface Container<DEVICE extends RealDeviceConnected<?, ?, ?, ?>, DEVICES extends RealList<? extends DEVICE, ?>> extends Device.Connected.Container<DEVICES> {
        void addConnectedDevice(DEVICE device);
    }
}

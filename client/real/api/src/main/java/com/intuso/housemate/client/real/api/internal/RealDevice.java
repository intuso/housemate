package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.comms.api.internal.payload.DeviceData;
import com.intuso.housemate.object.api.internal.Device;

/**
 * Base class for all devices
 */
public interface RealDevice<DRIVER extends DeviceDriver>
        extends Device<
        RealCommand,
        RealCommand,
        RealCommand,
        RealCommand,
        RealList<RealCommand>,
        RealValue<Boolean>,
        RealValue<String>,
        RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>>,
        RealValue<Boolean>,
        RealValue<?>,
        RealList<RealValue<?>>,
        RealProperty<?>,
        RealList<RealProperty<?>>,
        RealDevice<DRIVER>>, DeviceDriver.Callback {

    DRIVER getDriver();

    boolean isDriverLoaded();

    interface Container extends Device.Container<RealList<RealDevice<?>>>, RemoveCallback {
        <DRIVER extends DeviceDriver> RealDevice<DRIVER> createAndAddDevice(DeviceData data);
        void addDevice(RealDevice<?> device);
    }

    interface RemoveCallback {
        void removeDevice(RealDevice<?> device);
    }

    interface Factory {
        RealDevice<?> create(DeviceData data, RemoveCallback removeCallback);
    }
}

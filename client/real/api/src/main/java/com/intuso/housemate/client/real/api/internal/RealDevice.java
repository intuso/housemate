package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.comms.api.internal.payload.DeviceData;
import com.intuso.housemate.object.api.internal.Device;
import org.slf4j.Logger;

/**
 * Base class for all devices
 */
public interface RealDevice<DRIVER extends DeviceDriver>
        extends Device<
        RealCommand,
        RealCommand,
        RealCommand,
        RealValue<Boolean>,
        RealValue<String>,
        RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>>,
        RealValue<Boolean>,
        RealList<RealProperty<?>>,
        RealList<RealFeature>,
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
        RealDevice<?> create(Logger logger, DeviceData data, RemoveCallback removeCallback);
    }
}

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
                RealList<? extends RealCommand>,
                RealValue<Boolean>,
                RealValue<String>,
                RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>>,
                RealValue<Boolean>,
                RealValue<?>,
                RealList<? extends RealValue<?>>,
                RealProperty<?>,
                RealList<? extends RealProperty<?>>,
                RealDevice<DRIVER>>, DeviceDriver.Callback {

    DRIVER getDriver();

    boolean isDriverLoaded();

    interface RemovedListener {
        void deviceRemoved(RealDevice device);
    }

    interface Factory {
        RealDevice<?> create(DeviceData data, RemovedListener removedListener);
    }
}

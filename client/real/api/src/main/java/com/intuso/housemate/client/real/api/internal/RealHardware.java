package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.real.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.comms.api.internal.payload.HardwareData;
import com.intuso.housemate.object.api.internal.Hardware;

/**
 * Base class for all hardwares
 */
public interface RealHardware<DRIVER extends HardwareDriver>
        extends Hardware<
        RealCommand,
        RealCommand,
        RealCommand,
        RealValue<Boolean>,
        RealValue<String>,
        RealProperty<PluginResource<HardwareDriver.Factory<DRIVER>>>,
        RealValue<Boolean>,
        RealList<RealProperty<?>>,
        RealHardware<DRIVER>>,HardwareDriver.Callback {

    DRIVER getDriver();

    boolean isDriverLoaded();

    boolean isRunning();

    interface Container extends Hardware.Container<RealList<RealHardware<?>>>, RemoveCallback {
        <DRIVER extends HardwareDriver> RealHardware<DRIVER> createAndAddHardware(HardwareData data);
        void addHardware(RealHardware<?> hardware);
    }

    interface RemoveCallback {
        void removeHardware(RealHardware hardware);
    }

    interface Factory {
        RealHardware<?> create(HardwareData data, RemoveCallback removeCallback);
    }
}

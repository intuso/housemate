package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.real.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;

/**
 * Base class for all hardwares
 */
public interface RealHardware<DRIVER extends HardwareDriver,
        COMMAND extends RealCommand<?, ?, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        DRIVER_PROPERTY extends RealProperty<PluginResource<HardwareDriver.Factory<DRIVER>>, ?, ?, ?>,
        PROPERTIES extends com.intuso.housemate.client.real.api.internal.RealList<? extends RealProperty<?, ?, ?, ?>, ?>,
        HARDWARE extends RealHardware<DRIVER, COMMAND, BOOLEAN_VALUE, STRING_VALUE, DRIVER_PROPERTY, PROPERTIES, HARDWARE>>
        extends Hardware<COMMAND,
                        COMMAND,
                        COMMAND,
                        BOOLEAN_VALUE,
                        STRING_VALUE,
                        DRIVER_PROPERTY,
                        BOOLEAN_VALUE,
                        PROPERTIES,
                        HARDWARE>,
        HardwareDriver.Callback {

    DRIVER getDriver();

    boolean isDriverLoaded();

    boolean isRunning();

    interface Container<HARDWARE extends RealHardware<?, ?, ?, ?, ?, ?, ?>, HARDWARES extends com.intuso.housemate.client.real.api.internal.RealList<? extends HARDWARE, ?>> extends Hardware.Container<HARDWARES>, RemoveCallback<HARDWARE> {
        void addHardware(HARDWARE hardware);
    }

    interface RemoveCallback<HARDWARE extends RealHardware<?, ?, ?, ?, ?, ?, ?>> {
        void removeHardware(HARDWARE hardware);
    }
}

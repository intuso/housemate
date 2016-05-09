package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;

/**
 * Base class for all devices
 */
public interface RealDevice<DRIVER extends DeviceDriver,
        COMMAND extends RealCommand<?, ?, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        DRIVER_PROPERTY extends com.intuso.housemate.client.real.api.internal.RealProperty<PluginResource<DeviceDriver.Factory<DRIVER>>, ?, ?, ?>,
        PROPERTIES extends com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealProperty<?, ?, ?, ?>, ?>,
        FEATURES extends com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealFeature<?, ?, ?>, ?>,
        DEVICE extends RealDevice<DRIVER, COMMAND, BOOLEAN_VALUE, STRING_VALUE, DRIVER_PROPERTY, PROPERTIES, FEATURES, DEVICE>>
        extends Device<
                COMMAND,
                COMMAND,
                COMMAND,
                BOOLEAN_VALUE,
                STRING_VALUE,
                DRIVER_PROPERTY,
                BOOLEAN_VALUE,
                PROPERTIES,
                FEATURES,
                DEVICE>,
        DeviceDriver.Callback {

    DRIVER getDriver();

    boolean isDriverLoaded();

    interface Container<DEVICE extends RealDevice<?, ?, ?, ?, ?, ?, ?, ?>, DEVICES extends com.intuso.housemate.client.real.api.internal.RealList<? extends DEVICE, ?>> extends Device.Container<DEVICES>, RemoveCallback<DEVICE> {
        void addDevice(DEVICE device);
    }

    interface RemoveCallback<DEVICE extends RealDevice<?, ?, ?, ?, ?, ?, ?, ?>> {
        void removeDevice(DEVICE device);
    }
}

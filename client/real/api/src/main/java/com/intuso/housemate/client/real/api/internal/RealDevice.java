package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.plugin.api.internal.driver.DeviceDriver;
import com.intuso.housemate.plugin.api.internal.driver.PluginDependency;

/**
 * Base class for all devices
 */
public interface RealDevice<COMMAND extends RealCommand<?, ?, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        DRIVER_PROPERTY extends RealProperty<PluginDependency<DeviceDriver.Factory<?>>, ?, ?, ?>,
        PROPERTIES extends RealList<? extends RealProperty<?, ?, ?, ?>, ?>,
        FEATURES extends RealList<? extends RealFeature<?, ?, ?>, ?>,
        DEVICE extends RealDevice<COMMAND, BOOLEAN_VALUE, STRING_VALUE, DRIVER_PROPERTY, PROPERTIES, FEATURES, DEVICE>>
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

    <DRIVER extends DeviceDriver> DRIVER getDriver();

    boolean isDriverLoaded();

    interface Container<DEVICE extends RealDevice<?, ?, ?, ?, ?, ?, ?>, DEVICES extends RealList<? extends DEVICE, ?>> extends Device.Container<DEVICES>, RemoveCallback<DEVICE> {
        void addDevice(DEVICE device);
    }

    interface RemoveCallback<DEVICE extends RealDevice<?, ?, ?, ?, ?, ?, ?>> {
        void removeDevice(DEVICE device);
    }
}

package com.intuso.housemate.client.real.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Device;

/**
 * Base class for all devices
 */
public interface RealDevice<COMMAND extends RealCommand<?, ?, ?>,
        BOOLEAN_VALUE extends RealValue<Boolean, ?, ?>,
        STRING_VALUE extends RealValue<String, ?, ?>,
        FEATURES extends RealList<? extends RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        DEVICE extends RealDevice<COMMAND, BOOLEAN_VALUE, STRING_VALUE, FEATURES, DEVICE>>
        extends Device<
        COMMAND,
        COMMAND,
        COMMAND,
        BOOLEAN_VALUE,
        STRING_VALUE,
        FEATURES,
        DEVICE> {

    interface Container<DEVICE extends RealDevice<?, ?, ?, ?, ?>, DEVICES extends RealList<? extends DEVICE, ?>> extends Device.Container<DEVICES>, RemoveCallback<DEVICE> {
        void addDevice(DEVICE device);
    }

    interface RemoveCallback<DEVICE extends RealDevice<?, ?, ?, ?, ?>> {
        void removeDevice(DEVICE device);
    }
}

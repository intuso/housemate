package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Device;

/**
 * Base class for all devices
 */
public interface RealDevice<COMMAND extends RealCommand<?, ?, ?>,
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        PROPERTIES extends RealList<? extends RealProperty<?, ?, ?, ?>, ?>,
        DEVICE extends RealDevice<COMMAND, COMMANDS, VALUES, PROPERTIES, DEVICE>>
        extends Device<COMMAND,
                                COMMANDS,
                                VALUES,
                                PROPERTIES,
                                DEVICE> {

    interface Container<DEVICE extends RealDevice<?, ?, ?, ?, ?>, DEVICES extends RealList<? extends DEVICE, ?>> extends Device.Container<DEVICES> {
        void addDevice(DEVICE device);
    }
}

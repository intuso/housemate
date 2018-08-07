package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.DeviceComponent;

/**
 * Base class for all hardwares
 */
public interface RealDeviceComponent<
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        DEVICE_COMPONENT extends RealDeviceComponent<COMMANDS, VALUES, DEVICE_COMPONENT>>
        extends DeviceComponent<COMMANDS, VALUES, DEVICE_COMPONENT> {}

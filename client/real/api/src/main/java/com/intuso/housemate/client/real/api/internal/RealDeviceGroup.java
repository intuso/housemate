package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.view.DeviceGroupView;
import com.intuso.housemate.client.api.internal.object.view.DeviceView;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;

/**
 * Base class for all real device groups
 */
public interface RealDeviceGroup<
        RENAME_COMMAND extends RealCommand<?, ?, ?>,
        REMOVE_COMMAND extends RealCommand<?, ?, ?>,
        ADD_COMMAND extends RealCommand<?, ?, ?>,
        ERROR_VALUE extends RealValue<String, ?, ?>,
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        DEVICES extends RealList<? extends RealReference<DeviceView<?>, ? extends ProxyDevice<?, ?, ?, ?, ?, ?, ?>, ?>, ?>,
        DEVICE_GROUP extends RealDeviceGroup<RENAME_COMMAND, REMOVE_COMMAND, ADD_COMMAND, ERROR_VALUE, COMMANDS, VALUES, DEVICES, DEVICE_GROUP>>
        extends RealDevice<Device.Group.Data, Device.Group.Listener<? super DEVICE_GROUP>, RENAME_COMMAND, COMMANDS, VALUES, DeviceGroupView, DEVICE_GROUP>,
        Device.Group<RENAME_COMMAND, REMOVE_COMMAND, ADD_COMMAND, ERROR_VALUE, COMMANDS, VALUES, DEVICES, DEVICE_GROUP> {}

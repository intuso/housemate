package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;

/**
 * Base class for all real systems
 */
public interface RealDeviceCombi<
        RENAME_COMMAND extends RealCommand<?, ?, ?>,
        REMOVE_COMMAND extends RealCommand<?, ?, ?>,
        ADD_COMMAND extends RealCommand<?, ?, ?>,
        ERROR_VALUE extends RealValue<String, ?, ?>,
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        DEVICES extends RealList<? extends RealProperty<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>, ?, ?, ?>, ?>,
        DEVICE extends RealDeviceCombi<RENAME_COMMAND, REMOVE_COMMAND, ADD_COMMAND, ERROR_VALUE, COMMANDS, VALUES, DEVICES, DEVICE>>
        extends RealDevice<Device.Combi.Listener<? super DEVICE>, RENAME_COMMAND, COMMANDS, VALUES, DEVICE>,
        Device.Combi<RENAME_COMMAND, REMOVE_COMMAND, ADD_COMMAND, ERROR_VALUE, COMMANDS, VALUES, DEVICES, DEVICE> {}

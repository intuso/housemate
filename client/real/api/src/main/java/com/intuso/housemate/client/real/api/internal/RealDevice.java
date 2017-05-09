package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Device;

/**
 * Base class for all devices
 */
public interface RealDevice<DATA extends Device.Data,
        LISTENER extends Device.Listener<? super DEVICE>,
        COMMAND extends RealCommand<?, ?, ?>,
        COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        DEVICE extends RealDevice<DATA, LISTENER, COMMAND, COMMANDS, VALUES, DEVICE>>
        extends Device<DATA, LISTENER, COMMAND, COMMANDS, VALUES, DEVICE> {}

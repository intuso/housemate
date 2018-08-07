package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.view.DeviceView;

/**
 * Base class for all devices
 */
public interface RealDevice<DATA extends Device.Data,
        LISTENER extends Device.Listener<? super DEVICE>,
        COMMAND extends RealCommand<?, ?, ?>,
        DEVICE_COMPONENTS extends RealList<? extends RealDeviceComponent<?, ?, ?>, ?>,
        VIEW extends DeviceView<?>,
        DEVICE extends RealDevice<DATA, LISTENER, COMMAND, DEVICE_COMPONENTS, VIEW, DEVICE>>
        extends Device<DATA, LISTENER, COMMAND, DEVICE_COMPONENTS, VIEW, DEVICE> {}
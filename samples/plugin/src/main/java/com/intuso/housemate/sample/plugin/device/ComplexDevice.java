package com.intuso.housemate.sample.plugin.device;

import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.sample.plugin.CustomArg;
import com.intuso.utilities.log.Log;

/**
 * Example device with a non-standard constructor that cannot be used with the
 * {@link com.intuso.housemate.annotations.plugin.Devices} annotation so requires a factory
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginModule
 * @see ComplexDeviceFactory
 */
public class ComplexDevice extends RealDevice {

    private final CustomArg customArg;

    public ComplexDevice(Log log, String id, String name, String description, CustomArg customArg) {
        super(log, id, name, description);
        this.customArg = customArg;
    }
}

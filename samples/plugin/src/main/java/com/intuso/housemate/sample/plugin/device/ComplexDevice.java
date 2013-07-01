package com.intuso.housemate.sample.plugin.device;

import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;

/**
 * Complex device with a non-simple constructor that cannot be used with the
 * {@link com.intuso.housemate.annotations.plugin.Devices} annotation so requires a factory
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 * @see com.intuso.housemate.sample.plugin.device.ComplexDeviceFactory
 * @see com.intuso.housemate.sample.plugin.device.ReallyComplexDeviceFactory
 */
public class ComplexDevice extends RealDevice {
    public ComplexDevice(RealResources resources, String id, String name, String description, Object extraArg) {
        super(resources, id, name, description);
    }
}

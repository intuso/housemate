package com.intuso.housemate.sample.plugin.device;

import com.intuso.housemate.plugin.api.RealDeviceFactory;
import com.intuso.utilities.log.Log;

/**
 * Example factory for devices with a non-simple constructor. This factory has a simple
 * constructor so can be used with
 * {@link com.intuso.housemate.annotations.plugin.DeviceFactories} annotations
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class ComplexDeviceFactory implements RealDeviceFactory<ComplexDevice> {

    private final Object simpleArg = new Object();

    @Override
    public String getTypeId() {
        return "complex-device-factory";
    }

    @Override
    public String getTypeName() {
        return "Complex Device Factory";
    }

    @Override
    public String getTypeDescription() {
        return "A factory for complex devices";
    }

    @Override
    public ComplexDevice create(Log log, String id, String name, String description) {
        return new ComplexDevice(log, id, name, description, simpleArg);
    }
}

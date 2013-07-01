package com.intuso.housemate.sample.plugin.device;

import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.plugin.api.RealDeviceFactory;

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
    public ComplexDevice create(RealResources resources, String id, String name, String description) {
        return new ComplexDevice(resources, id, name, description, simpleArg);
    }
}

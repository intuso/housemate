package com.intuso.housemate.sample.plugin.device;

import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.plugin.api.RealDeviceFactory;

/**
 * Example factory for devices with a non-simple constructor. This factory also has a non-simple
 * constructor so must be manually added by overriding
 * {@link com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor#getDeviceFactories()}
 * and adding an instance of this to the resulting list
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class ReallyComplexDeviceFactory implements RealDeviceFactory<ComplexDevice> {

    private final Object complexArg;

    public ReallyComplexDeviceFactory(Object complexArg) {
        this.complexArg = complexArg;
    }

    @Override
    public String getTypeId() {
        return "really-complex-device-factory";
    }

    @Override
    public String getTypeName() {
        return "Really Complex Device Factory";
    }

    @Override
    public String getTypeDescription() {
        return "A complex factory for complex devices";
    }

    @Override
    public ComplexDevice create(RealResources resources, String id, String name, String description) {
        return new ComplexDevice(resources, id, name, description, complexArg);
    }
}

package com.intuso.housemate.sample.plugin.device;

import com.google.inject.Inject;
import com.intuso.housemate.plugin.api.RealDeviceFactory;
import com.intuso.housemate.sample.plugin.CustomArg;
import com.intuso.utilities.log.Log;

/**
 * Example factory for devices with a non-standard constructor.
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginModule
 * @see ComplexDevice
 */
public class ComplexDeviceFactory implements RealDeviceFactory<ComplexDevice> {

    private final CustomArg customArg;

    @Inject
    public ComplexDeviceFactory(CustomArg customArg) {
        this.customArg = customArg;
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
    public ComplexDevice create(Log log, String id, String name, String description) {
        return new ComplexDevice(log, id, name, description, customArg);
    }
}

package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.plugin.api.RealDeviceFactory;

import java.lang.reflect.Constructor;

/**
 */
public class SimpleDeviceFactory implements RealDeviceFactory<RealDevice> {

    private final FactoryInformation information;
    private final Constructor<? extends RealDevice> constructor;

    public SimpleDeviceFactory(FactoryInformation information, Constructor<? extends RealDevice> constructor) {
        this.information = information;
        this.constructor = constructor;
    }

    @Override
    public String getTypeId() {
        return information.id();
    }

    @Override
    public String getTypeName() {
        return information.name();
    }

    @Override
    public String getTypeDescription() {
        return information.description();
    }

    @Override
    public RealDevice create(RealResources resources, String id, String name, String description) throws HousemateException {
        try {
            return constructor.newInstance(resources, id, name, description);
        } catch(Exception e) {
            throw new HousemateException("Failed to create device", e);
        }
    }
}

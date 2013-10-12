package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealTask;
import com.intuso.housemate.object.broker.real.BrokerRealTaskOwner;
import com.intuso.housemate.plugin.api.BrokerTaskFactory;

import java.lang.reflect.Constructor;

/**
 */
public class SimpleTaskFactory implements BrokerTaskFactory<BrokerRealTask> {

    private final FactoryInformation information;
    private final Constructor<? extends BrokerRealTask> constructor;

    public SimpleTaskFactory(FactoryInformation information, Constructor<? extends BrokerRealTask> constructor) {
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
    public BrokerRealTask create(BrokerRealResources resources, String id, String name, String description,
                                 BrokerRealTaskOwner owner) throws HousemateException {
        try {
            return constructor.newInstance(resources, id, name, description, owner);
        } catch(Exception e) {
            throw new HousemateException("Failed to create task", e);
        }
    }
}

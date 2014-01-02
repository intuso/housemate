package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.plugin.api.ServerTaskFactory;

import java.lang.reflect.Constructor;

/**
 * Factory for simple tasks that have no special constructor
 */
public class SimpleTaskFactory implements ServerTaskFactory<ServerRealTask> {

    private final FactoryInformation information;
    private final Constructor<? extends ServerRealTask> constructor;

    public SimpleTaskFactory(FactoryInformation information, Constructor<? extends ServerRealTask> constructor) {
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
    public ServerRealTask create(ServerRealResources resources, String id, String name, String description,
                                 ServerRealTaskOwner owner) throws HousemateException {
        try {
            return constructor.newInstance(resources, id, name, description, owner);
        } catch(Exception e) {
            throw new HousemateException("Failed to create task", e);
        }
    }
}

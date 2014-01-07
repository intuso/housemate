package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.plugin.api.ServerConditionFactory;
import com.intuso.utilities.log.Log;

import java.lang.reflect.Constructor;

/**
 * Factory for simple conditions that have no special constructor
 */
public class SimpleConditionFactory implements ServerConditionFactory<ServerRealCondition> {

    private final FactoryInformation information;
    private final Constructor<? extends ServerRealCondition> constructor;

    public SimpleConditionFactory(FactoryInformation information, Constructor<? extends ServerRealCondition> constructor) {
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
    public ServerRealCondition create(Log log, String id, String name, String description,
                                      ServerRealConditionOwner owner, LifecycleHandler lifecycleHandler)
            throws HousemateException {
        try {
            return constructor.newInstance(log, id, name, description, owner, lifecycleHandler);
        } catch(Exception e) {
            throw new HousemateException("Failed to create condition", e);
        }
    }
}

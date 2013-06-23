package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealCondition;
import com.intuso.housemate.plugin.api.BrokerConditionFactory;

import java.lang.reflect.Constructor;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 02/06/13
 * Time: 22:39
 * To change this template use File | Settings | File Templates.
 */
public class SimpleConditionFactory implements BrokerConditionFactory<BrokerRealCondition> {

    private final FactoryInformation information;
    private final Constructor<? extends BrokerRealCondition> constructor;

    public SimpleConditionFactory(FactoryInformation information, Constructor<? extends BrokerRealCondition> constructor) {
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
    public BrokerRealCondition create(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
        try {
            return constructor.newInstance(resources, id, name, description);
        } catch(Exception e) {
            throw new HousemateException("Failed to create condition", e);
        }
    }
}

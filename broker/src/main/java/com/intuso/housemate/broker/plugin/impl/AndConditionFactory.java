package com.intuso.housemate.broker.plugin.impl;

import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.broker.object.real.condition.And;
import com.intuso.housemate.broker.plugin.BrokerConditionFactory;
import com.intuso.housemate.core.HousemateException;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class AndConditionFactory implements BrokerConditionFactory<And> {

    @Override
    public String getTypeId() {
        return "and";
    }

    @Override
    public String getTypeName() {
        return "And";
    }

    @Override
    public String getTypeDescription() {
        return "True only when all child conditions are true";
    }

    @Override
    public And create(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
        return new And(resources, id, name, description);
    }
}

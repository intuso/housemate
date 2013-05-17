package com.intuso.housemate.broker.plugin.impl;

import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.broker.object.real.condition.Not;
import com.intuso.housemate.broker.plugin.BrokerConditionFactory;
import com.intuso.housemate.core.HousemateException;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class NotConditionFactory implements BrokerConditionFactory<Not> {

    @Override
    public String getTypeId() {
        return "not";
    }

    @Override
    public String getTypeName() {
        return "Not";
    }

    @Override
    public String getTypeDescription() {
        return "Negation of the child condition";
    }

    @Override
    public Not create(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
        return new Not(resources, id, name, description);
    }
}

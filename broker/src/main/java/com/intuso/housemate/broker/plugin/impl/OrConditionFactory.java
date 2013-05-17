package com.intuso.housemate.broker.plugin.impl;

import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.broker.object.real.condition.Or;
import com.intuso.housemate.broker.plugin.BrokerConditionFactory;
import com.intuso.housemate.core.HousemateException;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class OrConditionFactory implements BrokerConditionFactory<Or> {

    @Override
    public String getTypeId() {
        return "or";
    }

    @Override
    public String getTypeName() {
        return "Or";
    }

    @Override
    public String getTypeDescription() {
        return "True if any child condition is true";
    }

    @Override
    public Or create(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
        return new Or(resources, id, name, description);
    }
}

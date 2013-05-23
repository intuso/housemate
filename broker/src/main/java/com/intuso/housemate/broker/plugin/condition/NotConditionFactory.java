package com.intuso.housemate.broker.plugin.condition;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.plugin.api.BrokerConditionFactory;

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

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

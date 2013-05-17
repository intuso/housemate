package com.intuso.housemate.broker.plugin.impl;

import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.broker.object.real.consequence.RandomDelay;
import com.intuso.housemate.broker.plugin.BrokerConsequenceFactory;
import com.intuso.housemate.core.HousemateException;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class RandomDelayConsequenceFactory implements BrokerConsequenceFactory<RandomDelay> {

    @Override
    public String getTypeId() {
        return "random-delay";
    }

    @Override
    public String getTypeName() {
        return "Random Delay";
    }

    @Override
    public String getTypeDescription() {
        return "Delays for a random amount of time";
    }

    @Override
    public RandomDelay create(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
        return new RandomDelay(resources, id, name, description);
    }
}

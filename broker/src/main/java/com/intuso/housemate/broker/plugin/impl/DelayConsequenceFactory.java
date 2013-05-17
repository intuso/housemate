package com.intuso.housemate.broker.plugin.impl;

import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.broker.object.real.consequence.Delay;
import com.intuso.housemate.broker.plugin.BrokerConsequenceFactory;
import com.intuso.housemate.core.HousemateException;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class DelayConsequenceFactory implements BrokerConsequenceFactory<Delay> {

    @Override
    public String getTypeId() {
        return "delay";
    }

    @Override
    public String getTypeName() {
        return "Delay";
    }

    @Override
    public String getTypeDescription() {
        return "Delays for a set amount of time";
    }

    @Override
    public Delay create(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
        return new Delay(resources, id, name, description);
    }
}

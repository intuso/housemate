package com.intuso.housemate.broker.plugin.consequence;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.plugin.api.BrokerConsequenceFactory;

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

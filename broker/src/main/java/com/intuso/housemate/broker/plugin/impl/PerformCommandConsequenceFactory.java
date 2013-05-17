package com.intuso.housemate.broker.plugin.impl;

import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.broker.object.real.consequence.PerformCommand;
import com.intuso.housemate.broker.plugin.BrokerConsequenceFactory;
import com.intuso.housemate.core.HousemateException;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class PerformCommandConsequenceFactory implements BrokerConsequenceFactory<PerformCommand> {

    @Override
    public String getTypeId() {
        return "perform-command";
    }

    @Override
    public String getTypeName() {
        return "Perform Command";
    }

    @Override
    public String getTypeDescription() {
        return "Perform a command in the system";
    }

    @Override
    public PerformCommand create(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
        return new PerformCommand(resources, id, name, description);
    }
}

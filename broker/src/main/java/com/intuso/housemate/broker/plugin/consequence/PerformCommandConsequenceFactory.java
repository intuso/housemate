package com.intuso.housemate.broker.plugin.consequence;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.plugin.api.BrokerConsequenceFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class PerformCommandConsequenceFactory implements BrokerConsequenceFactory<PerformCommand> {

    private final BrokerGeneralResources generalResources;

    public PerformCommandConsequenceFactory(BrokerGeneralResources generalResources) {
        this.generalResources = generalResources;
    }

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
        return new PerformCommand(resources, id, name, description, generalResources.getBridgeResources().getRoot());
    }
}

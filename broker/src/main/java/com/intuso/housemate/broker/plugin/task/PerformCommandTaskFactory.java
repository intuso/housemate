package com.intuso.housemate.broker.plugin.task;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.plugin.api.BrokerTaskFactory;

/**
 */
public class PerformCommandTaskFactory implements BrokerTaskFactory<PerformCommand> {

    private final BrokerGeneralResources generalResources;

    public PerformCommandTaskFactory(BrokerGeneralResources generalResources) {
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

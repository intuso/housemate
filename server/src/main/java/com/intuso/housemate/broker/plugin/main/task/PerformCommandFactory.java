package com.intuso.housemate.broker.plugin.main.task;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.broker.object.bridge.RootObjectBridge;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealTaskOwner;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.housemate.plugin.api.BrokerTaskFactory;

/**
 */
public class PerformCommandFactory implements BrokerTaskFactory<PerformCommand> {

    private final RootObjectBridge bridgeRoot;
    private final RealObjectType<BaseHousemateObject<?>> realObjectType;

    @Inject
    public PerformCommandFactory(RootObjectBridge bridgeRoot, RealObjectType<BaseHousemateObject<?>> realObjectType) {
        this.bridgeRoot = bridgeRoot;
        this.realObjectType = realObjectType;
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
    public PerformCommand create(BrokerRealResources resources, String id, String name, String description,
                                 BrokerRealTaskOwner owner) throws HousemateException {
        return new PerformCommand(resources, id, name, description, owner, bridgeRoot, realObjectType);
    }
}

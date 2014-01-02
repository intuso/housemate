package com.intuso.housemate.server.plugin.main.task;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.plugin.api.ServerTaskFactory;
import com.intuso.housemate.server.object.bridge.RootObjectBridge;
import com.intuso.housemate.object.real.impl.type.RealObjectType;

/**
 */
public class PerformCommandFactory implements ServerTaskFactory<PerformCommand> {

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
    public PerformCommand create(ServerRealResources resources, String id, String name, String description,
                                 ServerRealTaskOwner owner) throws HousemateException {
        return new PerformCommand(resources, id, name, description, owner, bridgeRoot, realObjectType);
    }
}

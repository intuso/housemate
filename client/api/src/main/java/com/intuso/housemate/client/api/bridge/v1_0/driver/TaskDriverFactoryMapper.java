package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.driver.TaskDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class TaskDriverFactoryMapper {

    private final TaskDriverFactoryBridge.Factory bridgeFactory;
    private final TaskDriverFactoryBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public TaskDriverFactoryMapper(TaskDriverFactoryBridge.Factory bridgeFactory, TaskDriverFactoryBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public <FROM extends com.intuso.housemate.client.api.internal.driver.TaskDriver, TO extends TaskDriver>
    Function<com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<FROM>, TaskDriver.Factory<TO>> getToV1_0Function() {
        return new Function<com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<FROM>, TaskDriver.Factory<TO>>() {
            @Override
            public TaskDriver.Factory<TO> apply(com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<FROM> taskDriverFactory) {
                return map(taskDriverFactory);
            }
        };
    }

    public <FROM extends TaskDriver, TO extends com.intuso.housemate.client.api.internal.driver.TaskDriver>
        Function<TaskDriver.Factory<FROM>, com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<TO>> getFromV1_0Function() {
        return new Function<TaskDriver.Factory<FROM>, com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<TO>>() {
            @Override
            public com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<TO> apply(TaskDriver.Factory<FROM> taskDriverFactory) {
                return map(taskDriverFactory);
            }
        };
    }

    public <FROM extends com.intuso.housemate.client.api.internal.driver.TaskDriver, TO extends TaskDriver>
        TaskDriver.Factory<TO> map(com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<FROM> taskDriverFactory) {
        if(taskDriverFactory == null)
            return null;
        else if(taskDriverFactory instanceof TaskDriverFactoryBridge)
            return (TaskDriver.Factory<TO>) ((TaskDriverFactoryBridge)taskDriverFactory).getFactory();
        return (TaskDriver.Factory<TO>) reverseBridgeFactory.create(taskDriverFactory);
    }

    public <FROM extends TaskDriver, TO extends com.intuso.housemate.client.api.internal.driver.TaskDriver>
        com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<TO> map(TaskDriver.Factory<FROM> taskDriverFactory) {
        if(taskDriverFactory == null)
            return null;
        else if(taskDriverFactory instanceof TaskDriverFactoryBridgeReverse)
            return (com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<TO>) ((TaskDriverFactoryBridgeReverse)taskDriverFactory).getFactory();
        return (com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<TO>) bridgeFactory.create(taskDriverFactory);
    }
}

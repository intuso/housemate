package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class ConditionDriverFactoryMapper {

    private final ConditionDriverFactoryBridge.Factory bridgeFactory;
    private final ConditionDriverFactoryBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public ConditionDriverFactoryMapper(ConditionDriverFactoryBridge.Factory bridgeFactory, ConditionDriverFactoryBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public <FROM extends com.intuso.housemate.client.real.api.internal.driver.ConditionDriver, TO extends ConditionDriver>
    Function<com.intuso.housemate.client.real.api.internal.driver.ConditionDriver.Factory<FROM>, ConditionDriver.Factory<TO>> getToV1_0Function() {
        return new Function<com.intuso.housemate.client.real.api.internal.driver.ConditionDriver.Factory<FROM>, ConditionDriver.Factory<TO>>() {
            @Override
            public ConditionDriver.Factory<TO> apply(com.intuso.housemate.client.real.api.internal.driver.ConditionDriver.Factory<FROM> conditionDriverFactory) {
                return map(conditionDriverFactory);
            }
        };
    }

    public <FROM extends ConditionDriver, TO extends com.intuso.housemate.client.real.api.internal.driver.ConditionDriver>
        Function<ConditionDriver.Factory<FROM>, com.intuso.housemate.client.real.api.internal.driver.ConditionDriver.Factory<TO>> getFromV1_0Function() {
        return new Function<ConditionDriver.Factory<FROM>, com.intuso.housemate.client.real.api.internal.driver.ConditionDriver.Factory<TO>>() {
            @Override
            public com.intuso.housemate.client.real.api.internal.driver.ConditionDriver.Factory<TO> apply(ConditionDriver.Factory<FROM> conditionDriverFactory) {
                return map(conditionDriverFactory);
            }
        };
    }

    public <FROM extends com.intuso.housemate.client.real.api.internal.driver.ConditionDriver, TO extends ConditionDriver>
        ConditionDriver.Factory<TO> map(com.intuso.housemate.client.real.api.internal.driver.ConditionDriver.Factory<FROM> conditionDriverFactory) {
        if(conditionDriverFactory == null)
            return null;
        else if(conditionDriverFactory instanceof ConditionDriverFactoryBridge)
            return (ConditionDriver.Factory<TO>) ((ConditionDriverFactoryBridge)conditionDriverFactory).getFactory();
        return (ConditionDriver.Factory<TO>) reverseBridgeFactory.create(conditionDriverFactory);
    }

    public <FROM extends ConditionDriver, TO extends com.intuso.housemate.client.real.api.internal.driver.ConditionDriver>
        com.intuso.housemate.client.real.api.internal.driver.ConditionDriver.Factory<TO> map(ConditionDriver.Factory<FROM> conditionDriverFactory) {
        if(conditionDriverFactory == null)
            return null;
        else if(conditionDriverFactory instanceof ConditionDriverFactoryBridgeReverse)
            return (com.intuso.housemate.client.real.api.internal.driver.ConditionDriver.Factory<TO>) ((ConditionDriverFactoryBridgeReverse)conditionDriverFactory).getFactory();
        return (com.intuso.housemate.client.real.api.internal.driver.ConditionDriver.Factory<TO>) bridgeFactory.create(conditionDriverFactory);
    }
}

package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.intuso.housemate.plugin.v1_0.api.driver.ConditionDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class ConditionDriverMapper {

    private final Function<com.intuso.housemate.plugin.api.internal.driver.ConditionDriver, ConditionDriver> toV1_0Function = new Function<com.intuso.housemate.plugin.api.internal.driver.ConditionDriver, ConditionDriver>() {
        @Override
        public ConditionDriver apply(com.intuso.housemate.plugin.api.internal.driver.ConditionDriver conditionDriver) {
            return map(conditionDriver);
        }
    };

    private final Function<ConditionDriver, com.intuso.housemate.plugin.api.internal.driver.ConditionDriver> fromV1_0Function = new Function<ConditionDriver, com.intuso.housemate.plugin.api.internal.driver.ConditionDriver>() {
        @Override
        public com.intuso.housemate.plugin.api.internal.driver.ConditionDriver apply(ConditionDriver conditionDriver) {
            return map(conditionDriver);
        }
    };

    public Function<com.intuso.housemate.plugin.api.internal.driver.ConditionDriver, ConditionDriver> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<ConditionDriver, com.intuso.housemate.plugin.api.internal.driver.ConditionDriver> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public <FROM extends com.intuso.housemate.plugin.api.internal.driver.ConditionDriver, TO extends ConditionDriver>
        TO map(FROM conditionDriver) {
        if(conditionDriver == null)
            return null;
        else if(conditionDriver instanceof ConditionDriverBridge)
            return (TO) ((ConditionDriverBridge) conditionDriver).getConditionDriver();
        return (TO) new ConditionDriverBridgeReverse(conditionDriver);
    }

    public <FROM extends ConditionDriver, TO extends com.intuso.housemate.plugin.api.internal.driver.ConditionDriver>
        TO map(FROM conditionDriver) {
        if(conditionDriver == null)
            return null;
        else if(conditionDriver instanceof ConditionDriverBridgeReverse)
            return (TO) ((ConditionDriverBridgeReverse) conditionDriver).getConditionDriver();
        return (TO) new ConditionDriverBridge(conditionDriver);
    }
}

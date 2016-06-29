package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.intuso.housemate.plugin.v1_0.api.driver.ConditionDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class ConditionDriverBridgeReverse implements ConditionDriver {

    private final com.intuso.housemate.plugin.api.internal.driver.ConditionDriver conditionDriver;

    public ConditionDriverBridgeReverse(com.intuso.housemate.plugin.api.internal.driver.ConditionDriver conditionDriver) {
        this.conditionDriver = conditionDriver;
    }

    public com.intuso.housemate.plugin.api.internal.driver.ConditionDriver getConditionDriver() {
        return conditionDriver;
    }

    @Override
    public void start() {
        conditionDriver.start();
    }

    @Override
    public void stop() {
        conditionDriver.stop();
    }

    @Override
    public boolean hasChildConditions() {
        return conditionDriver.hasChildConditions();
    }
}
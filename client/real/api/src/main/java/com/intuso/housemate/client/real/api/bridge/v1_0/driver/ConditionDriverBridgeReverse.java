package com.intuso.housemate.client.real.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class ConditionDriverBridgeReverse implements ConditionDriver {

    private final com.intuso.housemate.client.real.api.internal.driver.ConditionDriver conditionDriver;

    public ConditionDriverBridgeReverse(com.intuso.housemate.client.real.api.internal.driver.ConditionDriver conditionDriver) {
        this.conditionDriver = conditionDriver;
    }

    public com.intuso.housemate.client.real.api.internal.driver.ConditionDriver getConditionDriver() {
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

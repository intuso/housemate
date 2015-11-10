package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class ConditionDriverBridge implements ConditionDriver {

    private final com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver conditionDriver;

    public ConditionDriverBridge(com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver conditionDriver) {
        this.conditionDriver = conditionDriver;
    }

    public com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver getConditionDriver() {
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
package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.api.driver.ConditionDriver;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by tomc on 05/11/15.
 */
public class ConditionDriverBridge implements com.intuso.housemate.client.api.internal.driver.ConditionDriver {

    private final ConditionDriver conditionDriver;

    public ConditionDriverBridge(ConditionDriver conditionDriver) {
        this.conditionDriver = conditionDriver;
    }

    public ConditionDriver getConditionDriver() {
        return conditionDriver;
    }

    @Override
    public void init(Logger logger, com.intuso.housemate.client.api.internal.driver.ConditionDriver.Callback callback) {
        conditionDriver.init(logger, new CallbackBridge(callback));
    }

    @Override
    public void uninit() {
        conditionDriver.uninit();
    }

    @Override
    public boolean hasChildConditions() {
        return conditionDriver.hasChildConditions();
    }

    public static class CallbackBridge implements ConditionDriver.Callback {

        private final Callback callback;

        public CallbackBridge(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void setError(String error) {
            callback.setError(error);
        }

        @Override
        public void conditionSatisfied(boolean satisfied) {
            callback.conditionSatisfied(satisfied);
        }

        @Override
        public Map<String, Boolean> getChildSatisfied() {
            return callback.getChildSatisfied();
        }
    }
}

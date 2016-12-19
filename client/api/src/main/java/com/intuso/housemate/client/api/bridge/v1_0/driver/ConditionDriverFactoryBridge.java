package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.driver.ConditionDriver;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by tomc on 05/11/15.
 */
public class ConditionDriverFactoryBridge implements com.intuso.housemate.client.api.internal.driver.ConditionDriver.Factory<com.intuso.housemate.client.api.internal.driver.ConditionDriver> {

    private final ConditionDriver.Factory<?> factory;
    private final ConditionDriverMapper conditionDriverMapper;

    @Inject
    public ConditionDriverFactoryBridge(@Assisted ConditionDriver.Factory<?> factory,
                                        ConditionDriverMapper conditionDriverMapper) {
        this.factory = factory;
        this.conditionDriverMapper = conditionDriverMapper;
    }

    public ConditionDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public com.intuso.housemate.client.api.internal.driver.ConditionDriver create(Logger logger, com.intuso.housemate.client.api.internal.driver.ConditionDriver.Callback callback) {
        return conditionDriverMapper.map(factory.create(logger, new CallbackBridge(callback)));
    }

    private class CallbackBridge implements ConditionDriver.Callback {

        private final com.intuso.housemate.client.api.internal.driver.ConditionDriver.Callback callback;

        private CallbackBridge(com.intuso.housemate.client.api.internal.driver.ConditionDriver.Callback callback) {
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

    public interface Factory {
        ConditionDriverFactoryBridge create(ConditionDriver.Factory<?> conditionDriverFactory);
    }
}

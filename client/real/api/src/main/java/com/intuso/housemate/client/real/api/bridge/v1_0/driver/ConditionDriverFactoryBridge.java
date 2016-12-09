package com.intuso.housemate.client.real.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by tomc on 05/11/15.
 */
public class ConditionDriverFactoryBridge implements ConditionDriver.Factory<ConditionDriver> {

    private final com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver.Factory<?> factory;
    private final ConditionDriverMapper conditionDriverMapper;

    @Inject
    public ConditionDriverFactoryBridge(@Assisted com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver.Factory<?> factory,
                                        ConditionDriverMapper conditionDriverMapper) {
        this.factory = factory;
        this.conditionDriverMapper = conditionDriverMapper;
    }

    public com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public ConditionDriver create(Logger logger, ConditionDriver.Callback callback) {
        return conditionDriverMapper.map(factory.create(logger, new CallbackBridge(callback)));
    }

    private class CallbackBridge implements com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver.Callback {

        private final ConditionDriver.Callback callback;

        private CallbackBridge(ConditionDriver.Callback callback) {
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
        ConditionDriverFactoryBridge create(com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver.Factory<?> conditionDriverFactory);
    }
}

package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.driver.ConditionDriver;
import org.slf4j.Logger;

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
        return conditionDriverMapper.map(factory.create(logger, new ConditionDriverBridge.CallbackBridge(callback)));
    }

    public interface Factory {
        ConditionDriverFactoryBridge create(ConditionDriver.Factory<?> conditionDriverFactory);
    }
}

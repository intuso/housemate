package com.intuso.housemate.sample.plugin.condition;

import com.intuso.housemate.object.broker.real.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.BrokerRealConditionOwner;
import com.intuso.housemate.object.broker.real.BrokerRealResources;

/**
 * Complex condition with a non-simple constructor that cannot be used with the
 * {@link com.intuso.housemate.annotations.plugin.Conditions} annotation so requires a factory
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 * @see ComplexConditionFactory
 * @see ReallyComplexConditionFactory
 */
public class ComplexCondition extends BrokerRealCondition {

    private final Object extraArg;

    public ComplexCondition(BrokerRealResources resources, String id, String name, String description,
                            BrokerRealConditionOwner owner, Object extraArg) {
        super(resources, id, name, description, owner);
        this.extraArg = extraArg;
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}
}

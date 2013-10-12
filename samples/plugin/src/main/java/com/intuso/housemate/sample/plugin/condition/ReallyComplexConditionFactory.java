package com.intuso.housemate.sample.plugin.condition;

import com.intuso.housemate.object.broker.real.BrokerRealConditionOwner;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.plugin.api.BrokerConditionFactory;

/**
 * Example factory for conditions with a non-simple constructor. This factory also has a non-simple
 * constructor so must be manually added by overriding
 * {@link com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor#getConditionFactories()}
 * and adding an instance of this to the resulting list
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class ReallyComplexConditionFactory implements BrokerConditionFactory<ComplexCondition> {

    private final Object complexArg;

    public ReallyComplexConditionFactory(Object complexArg) {
        this.complexArg = complexArg;
    }

    @Override
    public String getTypeId() {
        return "really-complex-condition-factory";
    }

    @Override
    public String getTypeName() {
        return "Really Complex Condition Factory";
    }

    @Override
    public String getTypeDescription() {
        return "A complex factory for complex conditions";
    }

    @Override
    public ComplexCondition create(BrokerRealResources resources, String id, String name, String description,
                                   BrokerRealConditionOwner owner) {
        return new ComplexCondition(resources, id, name, description, owner, complexArg);
    }
}

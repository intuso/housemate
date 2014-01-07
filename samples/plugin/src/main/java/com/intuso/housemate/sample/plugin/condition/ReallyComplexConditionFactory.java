package com.intuso.housemate.sample.plugin.condition;

import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.plugin.api.ServerConditionFactory;
import com.intuso.utilities.log.Log;

/**
 * Example factory for conditions with a non-simple constructor. This factory also has a non-simple
 * constructor so must be manually added by overriding
 * {@link com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor#getConditionFactories()}
 * and adding an instance of this to the resulting list
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class ReallyComplexConditionFactory implements ServerConditionFactory<ComplexCondition> {

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
    public ComplexCondition create(Log log, String id, String name, String description,
                                   ServerRealConditionOwner owner, LifecycleHandler lifecycleHandler) {
        return new ComplexCondition(log, id, name, description, owner, lifecycleHandler, complexArg);
    }
}

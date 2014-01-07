package com.intuso.housemate.sample.plugin.condition;

import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.plugin.api.ServerConditionFactory;
import com.intuso.utilities.log.Log;

/**
 * Example factory for conditions with a non-simple constructor. This factory has a simple
 * constructor so can be used with
 * {@link com.intuso.housemate.annotations.plugin.ConditionFactories} annotations
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class ComplexConditionFactory implements ServerConditionFactory<ComplexCondition> {

    private final Object simpleArg = new Object();

    @Override
    public String getTypeId() {
        return "complex-condition-factory";
    }

    @Override
    public String getTypeName() {
        return "Complex Condition Factory";
    }

    @Override
    public String getTypeDescription() {
        return "A factory for complex conditions";
    }

    @Override
    public ComplexCondition create(Log log, String id, String name, String description,
                                   ServerRealConditionOwner owner, LifecycleHandler lifecycleHandler) {
        return new ComplexCondition(log, id, name, description, owner, lifecycleHandler, simpleArg);
    }
}

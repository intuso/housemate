package com.intuso.housemate.sample.plugin.condition;

import com.google.inject.Inject;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.plugin.api.ServerConditionFactory;
import com.intuso.housemate.sample.plugin.CustomArg;
import com.intuso.utilities.log.Log;

/**
 * Example factory for a condition with a non-standard constructor.
 *
 * @see ComplexCondition
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginModule
 */
public class ComplexConditionFactory implements ServerConditionFactory<ComplexCondition> {

    private final CustomArg customArg;

    @Inject
    public ComplexConditionFactory(CustomArg customArg) {
        this.customArg = customArg;
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
        return new ComplexCondition(log, id, name, description, owner, lifecycleHandler, customArg);
    }
}

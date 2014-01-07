package com.intuso.housemate.sample.plugin.condition;

import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.utilities.log.Log;

/**
 * Complex condition with a non-simple constructor that cannot be used with the
 * {@link com.intuso.housemate.annotations.plugin.Conditions} annotation so requires a factory
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 * @see ComplexConditionFactory
 * @see ReallyComplexConditionFactory
 */
public class ComplexCondition extends ServerRealCondition {

    private final Object extraArg;

    public ComplexCondition(Log log, String id, String name, String description,
                            ServerRealConditionOwner owner, LifecycleHandler lifecycleHandler, Object extraArg) {
        super(log, id, name, description, owner, lifecycleHandler);
        this.extraArg = extraArg;
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}
}

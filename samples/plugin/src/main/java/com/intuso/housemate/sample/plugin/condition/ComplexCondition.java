package com.intuso.housemate.sample.plugin.condition;

import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.sample.plugin.CustomArg;
import com.intuso.utilities.log.Log;

/**
 * Example condition with a non-standard constructor that cannot be used with the
 * {@link com.intuso.housemate.annotations.plugin.Conditions} annotation so requires a factory
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginModule
 * @see ComplexConditionFactory
 */
public class ComplexCondition extends ServerRealCondition {

    private final CustomArg customArg;

    public ComplexCondition(Log log, String id, String name, String description,
                            ServerRealConditionOwner owner, LifecycleHandler lifecycleHandler, CustomArg customArg) {
        super(log, id, name, description, owner, lifecycleHandler);
        this.customArg = customArg;
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}
}

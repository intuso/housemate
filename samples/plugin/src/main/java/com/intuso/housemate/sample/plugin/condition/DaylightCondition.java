package com.intuso.housemate.sample.plugin.condition;

import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.utilities.log.Log;

/**
 * Example condition with a simple constructor that can be used with the
 * {@link com.intuso.housemate.annotations.plugin.Conditions} annotation
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class DaylightCondition extends ServerRealCondition {

    public DaylightCondition(Log log, String id, String name, String description, ServerRealConditionOwner owner,
                             LifecycleHandler lifecycleHandler) {
        super(log, id, name, description, owner, lifecycleHandler);
    }

    @Override
    public void start() {
        // start checking for sunset/sunrise times, eg via a webservice or outdoor light sensor
    }

    @Override
    public void stop() {
        // stop checking sunset/sunrise times
    }
}

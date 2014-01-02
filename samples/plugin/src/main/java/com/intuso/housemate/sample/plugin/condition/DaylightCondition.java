package com.intuso.housemate.sample.plugin.condition;

import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.object.server.real.ServerRealResources;

/**
 * Example condition with a simple constructor that can be used with the
 * {@link com.intuso.housemate.annotations.plugin.Conditions} annotation
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class DaylightCondition extends ServerRealCondition {

    public DaylightCondition(ServerRealResources resources, String id, String name, String description, ServerRealConditionOwner owner) {
        super(resources, id, name, description, owner);
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

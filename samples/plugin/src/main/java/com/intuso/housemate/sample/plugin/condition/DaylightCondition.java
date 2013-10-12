package com.intuso.housemate.sample.plugin.condition;

import com.intuso.housemate.object.broker.real.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.BrokerRealConditionOwner;
import com.intuso.housemate.object.broker.real.BrokerRealResources;

/**
 * Example condition with a simple constructor that can be used with the
 * {@link com.intuso.housemate.annotations.plugin.Conditions} annotation
 *
 * @see com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor
 */
public class DaylightCondition extends BrokerRealCondition {

    public DaylightCondition(BrokerRealResources resources, String id, String name, String description, BrokerRealConditionOwner owner) {
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

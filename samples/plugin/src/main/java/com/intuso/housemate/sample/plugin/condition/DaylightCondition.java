package com.intuso.housemate.sample.plugin.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

@TypeInfo(id = "daylight-condition", name = "Daylight Condition", description = "Condition that is true when the sun is shining")
public class DaylightCondition extends ServerRealCondition {

    @Inject
    public DaylightCondition(Log log,
                             ListenersFactory listenersFactory,
                             @Assisted ConditionData data,
                             @Assisted ServerRealConditionOwner owner,
                             LifecycleHandler lifecycleHandler) {
        super(log, listenersFactory, data, owner, lifecycleHandler);
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

package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.object.real.impl.type.TimeType;
import com.intuso.housemate.plugin.api.ServerConditionFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class TimeOfTheDayFactory implements ServerConditionFactory<TimeOfTheDay> {

    private final TimeType timeType;

    @Inject
    public TimeOfTheDayFactory(TimeType timeType) {
        this.timeType = timeType;
    }

    @Override
    public String getTypeId() {
        return "time-of-the-day";
    }

    @Override
    public String getTypeName() {
        return "Time of the day";
    }

    @Override
    public String getTypeDescription() {
        return "Checks if the current time is within a set range";
    }

    @Override
    public TimeOfTheDay create(Log log, String id, String name, String description,
                                  ServerRealConditionOwner owner, LifecycleHandler lifecycleHandler) throws HousemateException {
        return new TimeOfTheDay(log, id, name, description, owner, lifecycleHandler, timeType);
    }
}

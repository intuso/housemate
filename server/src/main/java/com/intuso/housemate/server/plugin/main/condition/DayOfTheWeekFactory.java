package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.object.real.impl.type.DaysType;
import com.intuso.housemate.plugin.api.ServerConditionFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class DayOfTheWeekFactory implements ServerConditionFactory<DayOfTheWeek> {

    private final DaysType daysType;

    @Inject
    public DayOfTheWeekFactory(DaysType daysType) {
        this.daysType = daysType;
    }

    @Override
    public String getTypeId() {
        return "day-of-the-week";
    }

    @Override
    public String getTypeName() {
        return "Day of the week";
    }

    @Override
    public String getTypeDescription() {
        return "Check if the current day is within a set";
    }

    @Override
    public DayOfTheWeek create(Log log, String id, String name, String description,
                                  ServerRealConditionOwner owner, LifecycleHandler lifecycleHandler)
            throws HousemateException {
        return new DayOfTheWeek(log, id, name, description, owner, lifecycleHandler, daysType);
    }
}

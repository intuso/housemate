package com.intuso.housemate.object.real.impl.type;

import com.google.inject.Inject;
import com.intuso.utilities.log.Log;

/**
 * Type for a unit of time
 */
public class TimeUnitType extends EnumChoiceType<TimeUnit> {

    public final static String ID = "time-unit";
    public final static String NAME = "Time Unit";

    /**
     * @param log the log
     */
    @Inject
    public TimeUnitType(Log log) {
        super(log, ID, NAME, "A unit of time", 1, 1, TimeUnit.class, TimeUnit.values());
    }
}

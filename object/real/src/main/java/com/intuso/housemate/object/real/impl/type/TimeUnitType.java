package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.object.real.RealResources;

/**
 * Type for a unit of time
 */
public class TimeUnitType extends EnumSingleChoiceType<TimeUnit> {

    public final static String ID = "time-unit";
    public final static String NAME = "Time Unit";

    /**
     * @param resources the resources
     */
    public TimeUnitType(RealResources resources) {
        super(resources, ID, NAME, "A unit of time", TimeUnit.class, TimeUnit.values());
    }
}

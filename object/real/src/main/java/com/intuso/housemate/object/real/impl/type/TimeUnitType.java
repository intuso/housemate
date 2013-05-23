package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.object.real.RealResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 26/01/13
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
public class TimeUnitType extends EnumSingleChoiceType<TimeUnit> {

    public final static String ID = "time-unit";
    public final static String NAME = "Time Unit";

    public TimeUnitType(RealResources resources) {
        super(resources, ID, NAME, "A unit of time", TimeUnit.class, TimeUnit.values());
    }
}

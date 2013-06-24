package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.object.real.RealResources;

/**
 */
public class DaysType extends EnumMultiChoiceType<Day> {

    public final static String ID = "days";
    public final static String NAME = "Days";

    public DaysType(RealResources resources) {
        super(resources, ID, NAME, "Selection of days", Day.class, Day.values());
    }
}

package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.object.real.RealResources;

/**
 * Type for a set of days
 */
public class DaysType extends EnumMultiChoiceType<Day> {

    public final static String ID = "days";
    public final static String NAME = "Days";

    /**
     * @param resources {@inheritDoc}
     */
    public DaysType(RealResources resources) {
        super(resources, ID, NAME, "Selection of days", Day.class, Day.values());
    }
}

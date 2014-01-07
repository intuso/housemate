package com.intuso.housemate.object.real.impl.type;

import com.google.inject.Inject;
import com.intuso.utilities.log.Log;

/**
 * Type for a set of days
 */
public class DaysType extends EnumChoiceType<Day> {

    public final static String ID = "days";
    public final static String NAME = "Days";

    /**
     * @param log {@inheritDoc}
     */
    @Inject
    public DaysType(Log log) {
        super(log, ID, NAME, "Selection of days", 0, -1, Day.class, Day.values());
    }
}

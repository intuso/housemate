package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.type.Day;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Type for a set of days
 */
public class DaysType extends EnumChoiceType<Day> {

    public final static String ID = "days";
    public final static String NAME = "Days";

    private final static Logger logger = LoggerFactory.getLogger(DaysType.class);

    @Inject
    public DaysType(ListenersFactory listenersFactory) {
        super(logger, listenersFactory, ID, NAME, "Selection of days", 0, -1, Day.class, Day.values());
    }
}

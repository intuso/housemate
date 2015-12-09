package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.type.TimeUnit;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for a unit of time
 */
public class TimeUnitType extends EnumChoiceType<TimeUnit> {

    public final static String ID = "time-unit";
    public final static String NAME = "Time Unit";

    /**
     * @param logger the logger
     */
    @Inject
    public TimeUnitType(Logger logger, ListenersFactory listenersFactory) {
        super(logger, listenersFactory, ID, NAME, "A unit of time", 1, 1, TimeUnit.class, TimeUnit.values());
    }
}

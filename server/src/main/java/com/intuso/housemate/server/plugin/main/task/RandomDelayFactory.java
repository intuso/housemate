package com.intuso.housemate.server.plugin.main.task;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.TimeUnitType;
import com.intuso.housemate.plugin.api.ServerTaskFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class RandomDelayFactory implements ServerTaskFactory<RandomDelay> {

    private final TimeUnitType timeUnitType;
    private final IntegerType integerType;

    @Inject
    public RandomDelayFactory(TimeUnitType timeUnitType, IntegerType integerType) {
        this.timeUnitType = timeUnitType;
        this.integerType = integerType;
    }

    @Override
    public String getTypeId() {
        return "random-delay";
    }

    @Override
    public String getTypeName() {
        return "Random Delay";
    }

    @Override
    public String getTypeDescription() {
        return "Wait for a random amount of time";
    }

    @Override
    public RandomDelay create(Log log, String id, String name, String description,
                                 ServerRealTaskOwner owner) throws HousemateException {
        return new RandomDelay(log, id, name, description, owner, timeUnitType, integerType);
    }
}

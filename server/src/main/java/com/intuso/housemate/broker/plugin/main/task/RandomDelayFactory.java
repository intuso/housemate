package com.intuso.housemate.broker.plugin.main.task;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealTaskOwner;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.TimeUnitType;
import com.intuso.housemate.plugin.api.BrokerTaskFactory;

/**
 */
public class RandomDelayFactory implements BrokerTaskFactory<RandomDelay> {

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
    public RandomDelay create(BrokerRealResources resources, String id, String name, String description,
                                 BrokerRealTaskOwner owner) throws HousemateException {
        return new RandomDelay(resources, id, name, description, owner, timeUnitType, integerType);
    }
}

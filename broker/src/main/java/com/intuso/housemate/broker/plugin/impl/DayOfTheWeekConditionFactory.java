package com.intuso.housemate.broker.plugin.impl;

import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.broker.object.real.condition.DayOfTheWeek;
import com.intuso.housemate.broker.plugin.BrokerConditionFactory;
import com.intuso.housemate.core.HousemateException;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class DayOfTheWeekConditionFactory implements BrokerConditionFactory<DayOfTheWeek> {

    @Override
    public String getTypeId() {
        return "day-of-the-week";
    }

    @Override
    public String getTypeName() {
        return "Day of the Week";
    }

    @Override
    public String getTypeDescription() {
        return "True on certain days";
    }

    @Override
    public DayOfTheWeek create(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
        return new DayOfTheWeek(resources, id, name, description);
    }
}

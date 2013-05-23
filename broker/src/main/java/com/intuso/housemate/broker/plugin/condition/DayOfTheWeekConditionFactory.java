package com.intuso.housemate.broker.plugin.condition;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.plugin.api.BrokerConditionFactory;

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

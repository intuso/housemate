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
public class TimeOfTheDayConditionFactory implements BrokerConditionFactory<TimeOfTheDay> {

    @Override
    public String getTypeId() {
        return "time-of-the-day";
    }

    @Override
    public String getTypeName() {
        return "Time of the day";
    }

    @Override
    public String getTypeDescription() {
        return "True between two times";
    }

    @Override
    public TimeOfTheDay create(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
        return new TimeOfTheDay(resources, id, name, description);
    }
}

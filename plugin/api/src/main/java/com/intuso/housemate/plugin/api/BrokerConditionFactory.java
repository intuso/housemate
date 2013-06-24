package com.intuso.housemate.plugin.api;

import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealCondition;
import com.intuso.housemate.api.HousemateException;

/**
 */
public interface BrokerConditionFactory<C extends BrokerRealCondition> {
    public String getTypeId();
    public String getTypeName();
    public String getTypeDescription();
    public C create(BrokerRealResources resources, String id, String name, String description) throws HousemateException;
}

package com.intuso.housemate.broker.plugin;

import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.broker.object.real.condition.BrokerRealCondition;
import com.intuso.housemate.core.HousemateException;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/02/13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public interface BrokerConditionFactory<C extends BrokerRealCondition> {
    public String getTypeId();
    public String getTypeName();
    public String getTypeDescription();
    public C create(BrokerRealResources resources, String id, String name, String description) throws HousemateException;
}

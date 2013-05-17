package com.intuso.housemate.broker.plugin.impl;

import com.intuso.housemate.broker.object.real.BrokerRealResources;
import com.intuso.housemate.broker.object.real.condition.ValueComparison;
import com.intuso.housemate.broker.plugin.BrokerConditionFactory;
import com.intuso.housemate.core.HousemateException;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class ValueComparisonConditionFactory implements BrokerConditionFactory<ValueComparison> {

    @Override
    public String getTypeId() {
        return "value-comparison";
    }

    @Override
    public String getTypeName() {
        return "Value Comparison";
    }

    @Override
    public String getTypeDescription() {
        return "Compare a value";
    }

    @Override
    public ValueComparison create(BrokerRealResources resources, String id, String name, String description) throws HousemateException {
        return new ValueComparison(resources, id, name, description);
    }
}

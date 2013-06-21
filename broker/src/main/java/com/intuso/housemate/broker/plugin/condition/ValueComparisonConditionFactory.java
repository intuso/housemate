package com.intuso.housemate.broker.plugin.condition;

import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.plugin.api.BrokerConditionFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class ValueComparisonConditionFactory implements BrokerConditionFactory<ValueComparison> {

    private final BrokerGeneralResources generalResources;

    public ValueComparisonConditionFactory(BrokerGeneralResources generalResources) {
        this.generalResources = generalResources;
    }

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
        return new ValueComparison(resources, id, name, description, generalResources);
    }
}

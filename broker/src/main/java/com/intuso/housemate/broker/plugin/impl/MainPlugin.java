package com.intuso.housemate.broker.plugin.impl;

import com.intuso.housemate.broker.object.bridge.BridgeObject;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.BrokerConditionFactory;
import com.intuso.housemate.broker.plugin.BrokerConsequenceFactory;
import com.intuso.housemate.broker.plugin.PluginDescriptor;
import com.intuso.housemate.broker.plugin.RealDeviceFactory;
import com.intuso.housemate.real.RealType;
import com.intuso.housemate.real.impl.type.BooleanType;
import com.intuso.housemate.real.impl.type.DaysType;
import com.intuso.housemate.real.impl.type.DoubleType;
import com.intuso.housemate.real.impl.type.IntegerType;
import com.intuso.housemate.real.impl.type.RealObjectType;
import com.intuso.housemate.real.impl.type.StringType;
import com.intuso.housemate.real.impl.type.TimeType;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/02/13
 * Time: 09:02
 * To change this template use File | Settings | File Templates.
 */
public class MainPlugin implements PluginDescriptor {

    private BrokerGeneralResources resources;

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getDescription() {
        return "Plugin containing the core types and factories";
    }

    @Override
    public String getAuthor() {
        return "Intuso";
    }

    @Override
    public void init(BrokerGeneralResources resources) {
        this.resources = resources;
    }

    @Override
    public List<RealType<?, ?, ?>> getTypes() {
        return Arrays.<RealType<?, ?, ?>>asList(new StringType(resources.getClientResources()),
                new BooleanType(resources.getClientResources()),
                new IntegerType(resources.getClientResources()),
                new DoubleType(resources.getClientResources()),
                new RealObjectType<BridgeObject<?, ?, ?, ?, ?>>(resources.getClientResources(), resources.getBridgeResources().getRoot()),
                new TimeType(resources.getClientResources()),
                new DaysType(resources.getClientResources()));
    }

    @Override
    public List<RealDeviceFactory<?>> getDeviceFactories() {
        return Arrays.<RealDeviceFactory<?>>asList(new OnOffCommandDeviceFactory());
    }

    @Override
    public List<BrokerConditionFactory<?>> getConditionFactories() {
        return Arrays.<BrokerConditionFactory<?>>asList(new AndConditionFactory(),
                new OrConditionFactory(),
                new NotConditionFactory(),
                new ValueComparisonConditionFactory(),
                new TimeOfTheDayConditionFactory(),
                new DayOfTheWeekConditionFactory());
    }

    @Override
    public List<BrokerConsequenceFactory<?>> getConsequenceFactories() {
        return Arrays.<BrokerConsequenceFactory<?>>asList(new DelayConsequenceFactory(),
                new RandomDelayConsequenceFactory(),
                new PerformCommandConsequenceFactory());
    }
}

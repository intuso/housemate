package com.intuso.housemate.broker.plugin;

import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.condition.AndConditionFactory;
import com.intuso.housemate.broker.plugin.condition.DayOfTheWeekConditionFactory;
import com.intuso.housemate.broker.plugin.condition.NotConditionFactory;
import com.intuso.housemate.broker.plugin.condition.OrConditionFactory;
import com.intuso.housemate.broker.plugin.condition.TimeOfTheDayConditionFactory;
import com.intuso.housemate.broker.plugin.condition.ValueComparisonConditionFactory;
import com.intuso.housemate.broker.plugin.consequence.DelayConsequenceFactory;
import com.intuso.housemate.broker.plugin.consequence.PerformCommandConsequenceFactory;
import com.intuso.housemate.broker.plugin.consequence.RandomDelayConsequenceFactory;
import com.intuso.housemate.broker.plugin.device.OnOffCommandDeviceFactory;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.DaysType;
import com.intuso.housemate.object.real.impl.type.DoubleType;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.object.real.impl.type.TimeType;
import com.intuso.housemate.plugin.api.BrokerConditionFactory;
import com.intuso.housemate.plugin.api.BrokerConsequenceFactory;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.housemate.plugin.api.RealDeviceFactory;

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

    private final BrokerGeneralResources generalResources;

    public MainPlugin(BrokerGeneralResources generalResources) {
        this.generalResources = generalResources;
    }

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
    public void init(Resources resources) {}

    @Override
    public List<RealType<?, ?, ?>> getTypes(RealResources resources) {
        return Arrays.<RealType<?, ?, ?>>asList(new StringType(resources),
                new BooleanType(resources),
                new IntegerType(resources),
                new DoubleType(resources),
                new RealObjectType<BaseObject<?>>(resources, generalResources.getBridgeResources().getRoot()),
                new TimeType(resources),
                new DaysType(resources));
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
                new ValueComparisonConditionFactory(generalResources),
                new TimeOfTheDayConditionFactory(),
                new DayOfTheWeekConditionFactory());
    }

    @Override
    public List<BrokerConsequenceFactory<?>> getConsequenceFactories() {
        return Arrays.<BrokerConsequenceFactory<?>>asList(new DelayConsequenceFactory(),
                new RandomDelayConsequenceFactory(),
                new PerformCommandConsequenceFactory(generalResources));
    }
}

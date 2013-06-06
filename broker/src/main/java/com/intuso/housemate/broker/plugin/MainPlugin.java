package com.intuso.housemate.broker.plugin;

import com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor;
import com.intuso.housemate.annotations.plugin.Conditions;
import com.intuso.housemate.annotations.plugin.Consequences;
import com.intuso.housemate.annotations.plugin.Devices;
import com.intuso.housemate.annotations.plugin.PluginInformation;
import com.intuso.housemate.annotations.plugin.Types;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.condition.And;
import com.intuso.housemate.broker.plugin.condition.DayOfTheWeek;
import com.intuso.housemate.broker.plugin.condition.Not;
import com.intuso.housemate.broker.plugin.condition.Or;
import com.intuso.housemate.broker.plugin.condition.TimeOfTheDay;
import com.intuso.housemate.broker.plugin.condition.ValueComparisonConditionFactory;
import com.intuso.housemate.broker.plugin.consequence.Delay;
import com.intuso.housemate.broker.plugin.consequence.PerformCommandConsequenceFactory;
import com.intuso.housemate.broker.plugin.consequence.RandomDelay;
import com.intuso.housemate.broker.plugin.device.OnOffCommandDevice;
import com.intuso.housemate.broker.plugin.type.ValueSourceType;
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

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/02/13
 * Time: 09:02
 * To change this template use File | Settings | File Templates.
 */
@PluginInformation(id = "main-plugin", name = "Main plugin",
        description = "Plugin containing the core types and factories", author = "Intuso")
@Types({StringType.class, BooleanType.class, IntegerType.class, DoubleType.class, TimeType.class, DaysType.class})
@Devices({OnOffCommandDevice.class})
@Conditions({And.class, Or.class, Not.class, TimeOfTheDay.class, DayOfTheWeek.class})
@Consequences({Delay.class, RandomDelay.class})
public class MainPlugin extends AnnotatedPluginDescriptor {

    private final BrokerGeneralResources generalResources;

    public MainPlugin(BrokerGeneralResources generalResources) {
        this.generalResources = generalResources;
    }

    @Override
    public List<RealType<?, ?, ?>> getTypes(RealResources resources) throws HousemateException {
        List<RealType<?, ?, ?>> result = super.getTypes(resources);
        result.add(new RealObjectType<BaseObject<?>>(resources, generalResources.getBridgeResources().getRoot()));
        result.add(new ValueSourceType(resources, generalResources.getBridgeResources().getRoot()));
        return result;
    }

    @Override
    public List<BrokerConditionFactory<?>> getConditionFactories() {
        List<BrokerConditionFactory<?>> result = super.getConditionFactories();
        result.add(new ValueComparisonConditionFactory(generalResources));
        return result;
    }

    @Override
    public List<BrokerConsequenceFactory<?>> getConsequenceFactories() {
        List<BrokerConsequenceFactory<?>> result = super.getConsequenceFactories();
        result.add(new PerformCommandConsequenceFactory(generalResources));
        return result;
    }
}

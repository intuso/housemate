package com.intuso.housemate.broker.plugin;

import com.intuso.housemate.annotations.plugin.AnnotatedPluginDescriptor;
import com.intuso.housemate.annotations.plugin.Comparators;
import com.intuso.housemate.annotations.plugin.Conditions;
import com.intuso.housemate.annotations.plugin.Tasks;
import com.intuso.housemate.annotations.plugin.Devices;
import com.intuso.housemate.annotations.plugin.PluginInformation;
import com.intuso.housemate.annotations.plugin.Types;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.comparator.BooleanComparators;
import com.intuso.housemate.broker.plugin.comparator.DoubleComparators;
import com.intuso.housemate.broker.plugin.comparator.IntegerComparators;
import com.intuso.housemate.broker.plugin.comparator.StringComparators;
import com.intuso.housemate.broker.plugin.condition.And;
import com.intuso.housemate.broker.plugin.condition.DayOfTheWeek;
import com.intuso.housemate.broker.plugin.condition.Not;
import com.intuso.housemate.broker.plugin.condition.Or;
import com.intuso.housemate.broker.plugin.condition.TimeOfTheDay;
import com.intuso.housemate.broker.plugin.condition.ValueComparisonConditionFactory;
import com.intuso.housemate.broker.plugin.task.Delay;
import com.intuso.housemate.broker.plugin.task.PerformCommandTaskFactory;
import com.intuso.housemate.broker.plugin.task.RandomDelay;
import com.intuso.housemate.broker.plugin.device.OnOffCommandDevice;
import com.intuso.housemate.broker.plugin.type.comparison.ComparisonOperatorType;
import com.intuso.housemate.broker.plugin.type.comparison.ComparisonType;
import com.intuso.housemate.broker.plugin.type.constant.ConstantType;
import com.intuso.housemate.broker.plugin.type.valuesource.ValueSourceType;
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
import com.intuso.housemate.plugin.api.BrokerTaskFactory;

import java.util.List;

/**
 */
@PluginInformation(id = "main-plugin", name = "Main plugin",
        description = "Plugin containing the core types and factories", author = "Intuso")
@Types({StringType.class, BooleanType.class, IntegerType.class, DoubleType.class, TimeType.class, DaysType.class})
@Comparators({StringComparators.Equals.class, StringComparators.GreaterThan.class,
        StringComparators.GreaterThanOrEqual.class, StringComparators.LessThan.class,
        StringComparators.LessThanOrEqual.class, BooleanComparators.Equals.class, IntegerComparators.Equals.class,
        IntegerComparators.GreaterThan.class, IntegerComparators.GreaterThanOrEqual.class,
        IntegerComparators.LessThan.class, IntegerComparators.LessThanOrEqual.class, DoubleComparators.Equals.class,
        DoubleComparators.GreaterThan.class, DoubleComparators.GreaterThanOrEqual.class,
        DoubleComparators.LessThan.class, DoubleComparators.LessThanOrEqual.class})
@Devices({OnOffCommandDevice.class})
@Conditions({And.class, Or.class, Not.class, TimeOfTheDay.class, DayOfTheWeek.class})
@Tasks({Delay.class, RandomDelay.class})
public class MainPlugin extends AnnotatedPluginDescriptor {

    public static ConstantType CONSTANT_TYPE;

    private final BrokerGeneralResources generalResources;

    public MainPlugin(BrokerGeneralResources generalResources) {
        this.generalResources = generalResources;
    }

    @Override
    public List<RealType<?, ?, ?>> getTypes(RealResources resources) {
        List<RealType<?, ?, ?>> result = super.getTypes(resources);
        CONSTANT_TYPE = new ConstantType(generalResources.getClientResources(), generalResources.getClient().getRoot().getTypes());
        result.add(CONSTANT_TYPE);
        result.add(new RealObjectType<BaseObject<?>>(resources, generalResources.getBridgeResources().getRoot()));
        ComparisonOperatorType operatorType = new ComparisonOperatorType(resources, generalResources);
        ValueSourceType sourceType = new ValueSourceType(resources, generalResources.getBridgeResources().getRoot(), generalResources.getClient().getRoot().getTypes());
        result.add(sourceType);
        result.add(operatorType);
        result.add(new ComparisonType(resources, generalResources, operatorType, sourceType));
        return result;
    }

    @Override
    public List<BrokerConditionFactory<?>> getConditionFactories() {
        List<BrokerConditionFactory<?>> result = super.getConditionFactories();
        result.add(new ValueComparisonConditionFactory(generalResources));
        return result;
    }

    @Override
    public List<BrokerTaskFactory<?>> getTaskFactories() {
        List<BrokerTaskFactory<?>> result = super.getTaskFactories();
        result.add(new PerformCommandTaskFactory(generalResources));
        return result;
    }
}

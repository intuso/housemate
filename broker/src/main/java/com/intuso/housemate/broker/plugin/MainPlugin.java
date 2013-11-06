package com.intuso.housemate.broker.plugin;

import com.intuso.housemate.annotations.plugin.*;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.comparator.BooleanComparators;
import com.intuso.housemate.broker.plugin.comparator.DoubleComparators;
import com.intuso.housemate.broker.plugin.comparator.IntegerComparators;
import com.intuso.housemate.broker.plugin.comparator.StringComparators;
import com.intuso.housemate.broker.plugin.condition.*;
import com.intuso.housemate.broker.plugin.device.PowerByCommandDevice;
import com.intuso.housemate.broker.plugin.task.Delay;
import com.intuso.housemate.broker.plugin.task.PerformCommandTaskFactory;
import com.intuso.housemate.broker.plugin.task.RandomDelay;
import com.intuso.housemate.broker.plugin.type.comparison.ComparisonType;
import com.intuso.housemate.broker.plugin.type.comparison.ComparisonTypeType;
import com.intuso.housemate.broker.plugin.type.constant.ConstantType;
import com.intuso.housemate.broker.plugin.type.valuesource.ValueSourceType;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.*;
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
@Devices({PowerByCommandDevice.class})
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
        result.add(new RealObjectType<BaseHousemateObject<?>>(resources, generalResources.getBridgeResources().getRoot()));
        ComparisonTypeType comparisonTypeType = new ComparisonTypeType(resources, generalResources);
        result.add(comparisonTypeType);
//        OperationTypeType operationTypeType = new OperationTypeType(resources, generalResources);
//        result.add(operationTypeType);
        ValueSourceType sourceType = new ValueSourceType(resources, generalResources);
        result.add(sourceType);
        result.add(new ComparisonType(resources, generalResources, comparisonTypeType, sourceType));
//        result.add(new OperationType(resources, generalResources, operationTypeType, sourceType));
//        result.add(new TransformationType(resources, generalResources));
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

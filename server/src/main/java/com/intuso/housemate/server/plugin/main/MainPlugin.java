package com.intuso.housemate.server.plugin.main;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.annotations.plugin.*;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.plugin.api.ServerConditionFactory;
import com.intuso.housemate.plugin.api.ServerTaskFactory;
import com.intuso.housemate.server.plugin.main.comparator.BooleanComparators;
import com.intuso.housemate.server.plugin.main.comparator.DoubleComparators;
import com.intuso.housemate.server.plugin.main.comparator.IntegerComparators;
import com.intuso.housemate.server.plugin.main.comparator.StringComparators;
import com.intuso.housemate.server.plugin.main.condition.DayOfTheWeekFactory;
import com.intuso.housemate.server.plugin.main.condition.TimeOfTheDayFactory;
import com.intuso.housemate.server.plugin.main.condition.ValueComparisonFactory;
import com.intuso.housemate.server.plugin.main.device.PowerByCommandDevice;
import com.intuso.housemate.server.plugin.main.task.DelayFactory;
import com.intuso.housemate.server.plugin.main.task.PerformCommandFactory;
import com.intuso.housemate.server.plugin.main.task.RandomDelayFactory;
import com.intuso.housemate.server.plugin.main.type.comparison.ComparisonType;
import com.intuso.housemate.server.plugin.main.type.comparison.ComparisonTypeType;
import com.intuso.housemate.server.plugin.main.type.constant.ConstantType;
import com.intuso.housemate.server.plugin.main.type.operation.OperationType;
import com.intuso.housemate.server.plugin.main.type.operation.OperationTypeType;
import com.intuso.housemate.server.plugin.main.type.transformation.TransformationOutputType;
import com.intuso.housemate.server.plugin.main.type.transformation.TransformationType;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSourceType;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.*;

import java.util.List;

/**
 */
@PluginInformation(id = "main-plugin",
        name = "Main plugin",
        description = "Plugin containing the core types and factories",
        author = "Intuso")
@Types({StringType.class,
        BooleanType.class,
        IntegerType.class,
        DoubleType.class,
        TimeType.class,
        DaysType.class})
@Comparators({StringComparators.Equals.class,
        StringComparators.GreaterThan.class,
        StringComparators.GreaterThanOrEqual.class,
        StringComparators.LessThan.class,
        StringComparators.LessThanOrEqual.class,
        BooleanComparators.Equals.class,
        IntegerComparators.Equals.class,
        IntegerComparators.GreaterThan.class,
        IntegerComparators.GreaterThanOrEqual.class,
        IntegerComparators.LessThan.class,
        IntegerComparators.LessThanOrEqual.class,
        DoubleComparators.Equals.class,
        DoubleComparators.GreaterThan.class,
        DoubleComparators.GreaterThanOrEqual.class,
        DoubleComparators.LessThan.class,
        DoubleComparators.LessThanOrEqual.class})
@Devices({PowerByCommandDevice.class})
@Conditions({com.intuso.housemate.server.plugin.main.condition.And.class,
        com.intuso.housemate.server.plugin.main.condition.Or.class,
        com.intuso.housemate.server.plugin.main.condition.Not.class})
@Tasks({})
public class MainPlugin extends AnnotatedPluginDescriptor {

    private final Injector injector;

    public MainPlugin(Injector injector) {
        this.injector = injector.createChildInjector(new MainPluginModule());
    }

    @Override
    public List<RealType<?, ?, ?>> getTypes(RealResources resources) {
        List<RealType<?, ?, ?>> result = super.getTypes(resources);
        result.add(injector.getInstance(ConstantType.class));
        result.add(injector.getInstance(new Key<RealObjectType<BaseHousemateObject<?>>>() {}));
        result.add(injector.getInstance(ValueSourceType.class));
        result.add(injector.getInstance(ComparisonTypeType.class));
        result.add(injector.getInstance(ComparisonType.class));
        result.add(injector.getInstance(OperationTypeType.class));
        result.add(injector.getInstance(OperationType.class));
        result.add(injector.getInstance(TransformationOutputType.class));
        result.add(injector.getInstance(TransformationType.class));
        return result;
    }

    @Override
    public List<ServerConditionFactory<?>> getConditionFactories() {
        List<ServerConditionFactory<?>> result = super.getConditionFactories();
        result.add(injector.getInstance(DayOfTheWeekFactory.class));
        result.add(injector.getInstance(TimeOfTheDayFactory.class));
        result.add(injector.getInstance(ValueComparisonFactory.class));
        return result;
    }

    @Override
    public List<ServerTaskFactory<?>> getTaskFactories() {
        List<ServerTaskFactory<?>> result = super.getTaskFactories();
        result.add(injector.getInstance(DelayFactory.class));
        result.add(injector.getInstance(RandomDelayFactory.class));
        result.add(injector.getInstance(PerformCommandFactory.class));
        return result;
    }
}

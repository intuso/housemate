package com.intuso.housemate.server.plugin.main;

import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.annotations.plugin.*;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.*;
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
import com.intuso.housemate.server.plugin.main.type.operation.Operation;
import com.intuso.housemate.server.plugin.main.type.operation.OperationType;
import com.intuso.housemate.server.plugin.main.type.operation.OperationTypeType;
import com.intuso.housemate.server.plugin.main.type.transformation.Transformation;
import com.intuso.housemate.server.plugin.main.type.transformation.TransformationOutputType;
import com.intuso.housemate.server.plugin.main.type.transformation.TransformationType;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSourceType;
import com.intuso.utilities.log.Log;

/**
 */
@PluginInformation(id = "main-plugin",
        name = "Main plugin",
        description = "Plugin containing the core types and factories")
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
public class MainPluginModule extends AnnotatedPluginModule {

    @Inject
    public MainPluginModule(Log log, Injector injector) {
        super(log);
    }

    @Override
    public void configure() {

        super.configure();
        
        // mark all as singletons
        bind(ValueComparisonFactory.class).in(Scopes.SINGLETON);
        bind(com.intuso.housemate.server.plugin.main.type.comparison.ComparisonType.class).in(Scopes.SINGLETON);
        bind(ComparisonTypeType.class).in(Scopes.SINGLETON);
        bind(ConstantType.class).in(Scopes.SINGLETON);
        bind(OperationType.class).in(Scopes.SINGLETON);
        bind(OperationTypeType.class).in(Scopes.SINGLETON);
        bind(TransformationOutputType.class).in(Scopes.SINGLETON);
        bind(TransformationType.class).in(Scopes.SINGLETON);
        bind(ValueSourceType.class).in(Scopes.SINGLETON);

        // bind implementations
        bind(new TypeLiteral<TypeSerialiser<ValueSource>>() {}).to(ValueSourceType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<com.intuso.housemate.plugin.api.ComparisonType>>() {}).to(ComparisonTypeType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<Operation>>() {}).to(OperationType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<com.intuso.housemate.plugin.api.OperationType>>() {}).to(OperationTypeType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<String>>() {}).to(TransformationOutputType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<Transformation>>() {}).to(TransformationType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<RealObjectType.Reference<?>>>() {}).to(new Key<RealObjectType.Serialiser<?>>() {}).in(Scopes.SINGLETON);
    }

    @Override
    public void configureTypes(Multibinder<RealType<?, ?, ?>> typeBindings) {
        super.configureTypes(typeBindings);
        typeBindings.addBinding().to(ConstantType.class);
        typeBindings.addBinding().to(new Key<RealObjectType<BaseHousemateObject<?>>>() {});
        typeBindings.addBinding().to(ValueSourceType.class);
        typeBindings.addBinding().to(ComparisonTypeType.class);
        typeBindings.addBinding().to(ComparisonType.class);
        typeBindings.addBinding().to(OperationTypeType.class);
        typeBindings.addBinding().to(OperationType.class);
        typeBindings.addBinding().to(TransformationOutputType.class);
        typeBindings.addBinding().to(TransformationType.class);
    }

    @Override
    public void configureConditionFactories(Multibinder<ServerConditionFactory<?>> conditionFactoryBindings) {
        super.configureConditionFactories(conditionFactoryBindings);
        conditionFactoryBindings.addBinding().to(DayOfTheWeekFactory.class);
        conditionFactoryBindings.addBinding().to(TimeOfTheDayFactory.class);
        conditionFactoryBindings.addBinding().to(ValueComparisonFactory.class);
    }

    @Override
    public void configureTaskFactories(Multibinder<ServerTaskFactory<?>> taskFactoryBindings) {
        super.configureTaskFactories(taskFactoryBindings);
        taskFactoryBindings.addBinding().to(DelayFactory.class);
        taskFactoryBindings.addBinding().to(RandomDelayFactory.class);
        taskFactoryBindings.addBinding().to(PerformCommandFactory.class);
    }
}

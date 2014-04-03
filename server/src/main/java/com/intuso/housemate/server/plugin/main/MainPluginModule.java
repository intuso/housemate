package com.intuso.housemate.server.plugin.main;

import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.*;
import com.intuso.housemate.plugin.api.*;
import com.intuso.housemate.server.plugin.main.comparator.BooleanComparators;
import com.intuso.housemate.server.plugin.main.comparator.DoubleComparators;
import com.intuso.housemate.server.plugin.main.comparator.IntegerComparators;
import com.intuso.housemate.server.plugin.main.comparator.StringComparators;
import com.intuso.housemate.server.plugin.main.condition.*;
import com.intuso.housemate.server.plugin.main.device.PowerByCommandDevice;
import com.intuso.housemate.server.plugin.main.operator.DoubleOperators;
import com.intuso.housemate.server.plugin.main.operator.IntegerOperators;
import com.intuso.housemate.server.plugin.main.task.Delay;
import com.intuso.housemate.server.plugin.main.task.PerformCommand;
import com.intuso.housemate.server.plugin.main.task.RandomDelay;
import com.intuso.housemate.server.plugin.main.transformer.FromBoolean;
import com.intuso.housemate.server.plugin.main.transformer.FromDouble;
import com.intuso.housemate.server.plugin.main.transformer.FromInteger;
import com.intuso.housemate.server.plugin.main.transformer.FromString;
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
@TypeInfo(id = "main-plugin",
        name = "Main plugin",
        description = "Plugin containing the core types and factories")
@Types({StringType.class,
        BooleanType.class,
        IntegerType.class,
        DoubleType.class,
        TimeType.class,
        DaysType.class,
        ConstantType.class,
        ValueSourceType.class,
        ComparisonTypeType.class,
        ComparisonType.class,
        OperationTypeType.class,
        OperationType.class,
        TransformationOutputType.class,
        TransformationType.class})
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
@Operators({IntegerOperators.Divide.class,
        IntegerOperators.Max.class,
        IntegerOperators.Min.class,
        IntegerOperators.Minus.class,
        IntegerOperators.Plus.class,
        IntegerOperators.Times.class,
        DoubleOperators.Divide.class,
        DoubleOperators.Max.class,
        DoubleOperators.Min.class,
        DoubleOperators.Minus.class,
        DoubleOperators.Plus.class,
        DoubleOperators.Times.class})
@Transformers({FromBoolean.ToDouble.class,
        FromBoolean.ToInteger.class,
        FromBoolean.ToString.class,
        FromDouble.ToInteger.class,
        FromDouble.ToBoolean.class,
        FromDouble.ToString.class,
        FromInteger.ToDouble.class,
        FromInteger.ToBoolean.class,
        FromInteger.ToString.class,
        FromString.ToDouble.class,
        FromString.ToBoolean.class,
        FromString.ToInteger.class})
@Devices({PowerByCommandDevice.class})
@Conditions({And.class,
        Or.class,
        Not.class,
        DayOfTheWeek.class,
        TimeOfTheDay.class,
        ValueComparison.class})
@Tasks({Delay.class,
        RandomDelay.class,
        PerformCommand.class})
public class MainPluginModule extends PluginModule {

    @Inject
    public MainPluginModule(Log log) {
        super(log);
    }

    @Override
    public void configure() {

        super.configure();
        
        // mark all as singletons
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
        bind(new TypeLiteral<TypeSerialiser<com.intuso.housemate.plugin.api.OperationType>>() {}).to(OperationTypeType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<Operation>>() {}).to(OperationType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<RealType<?, ?, ?>>>() {}).to(TransformationOutputType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<Transformation>>() {}).to(TransformationType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<RealObjectType.Reference<?>>>() {}).to(new Key<RealObjectType.Serialiser<?>>() {}).in(Scopes.SINGLETON);
    }

    @Override
    public void configureTypes(Multibinder<RealType<?, ?, ?>> typeBindings) {
        super.configureTypes(typeBindings);
        typeBindings.addBinding().to(new Key<RealObjectType<BaseHousemateObject<?>>>() {});
    }
}

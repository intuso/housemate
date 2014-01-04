package com.intuso.housemate.server.plugin.main;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.server.plugin.main.condition.ValueComparisonFactory;
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
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.housemate.plugin.api.ComparisonType;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/12/13
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public class MainPluginModule extends AbstractModule {

    @Override
    protected void configure() {

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
        bind(new TypeLiteral<TypeSerialiser<ComparisonType>>() {}).to(ComparisonTypeType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<Operation>>() {}).to(OperationType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<com.intuso.housemate.plugin.api.OperationType>>() {}).to(OperationTypeType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<String>>() {}).to(TransformationOutputType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<Transformation>>() {}).to(TransformationType.Serialiser.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<TypeSerialiser<RealObjectType.Reference<?>>>() {}).to(new Key<RealObjectType.Serialiser<?>>() {
        }).in(Scopes.SINGLETON);
    }
}

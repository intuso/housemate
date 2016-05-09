package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.client.real.impl.internal.type.*;

import java.util.Map;

/**
 * Created by tomc on 04/05/16.
 */
public class RealTypesModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(new TypeLiteral<Map<String, RealTypeImpl.Factory<?>>>() {}).toProvider(TypeFactoriesProvider.class);

        // bind everything as singletons that should be
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealTypeImpl.Factory<BooleanType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealTypeImpl.Factory<DaysType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealTypeImpl.Factory<DoubleType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealTypeImpl.Factory<EmailType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealTypeImpl.Factory<IntegerType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealTypeImpl.Factory<StringType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealTypeImpl.Factory<TimeType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealTypeImpl.Factory<TimeUnitType>>() {}));
    }
}

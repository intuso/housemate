package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.real.impl.internal.*;

/**
 * Created by tomc on 20/03/15.
 */
public class RealObjectsModule extends AbstractModule {
    @Override
    protected void configure() {

        // automations
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealAutomationImpl.Factory>() {}));

        // commands
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealCommandImpl.Factory>() {}));

        // condition
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealConditionImpl.Factory>() {}));

        // devices
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealDeviceImpl.Factory>() {}));

        // features
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealFeatureImpl.Factory>() {}));

        // tasks
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealTaskImpl.Factory>() {}));

        // lists
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealAutomationImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealCommandImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealConditionImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealDeviceImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealFeatureImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealHardwareImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealNodeImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealParameterImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealPropertyImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealSubTypeImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealTaskImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealTypeImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealUserImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListImpl.Factory<RealValueImpl<?>>>() {}));

        // hardwares
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealHardwareImpl.Factory>() {}));

        // nodes
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealNodeImpl.Factory>() {}));

        // options
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealOptionImpl.Factory>() {}));

        // users
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealUserImpl.Factory>() {}));
    }
}

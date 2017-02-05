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

        // lists
        // generated
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealAutomationImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealCommandImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealConditionImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealDeviceImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealHardwareImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealOptionImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealParameterImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealPropertyImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealSubTypeImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealSystemImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealTaskImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealTypeImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealUserImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealValueImpl<?>>>() {}));
        // persisted
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealAutomationImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealCommandImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealConditionImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealDeviceImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealHardwareImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealOptionImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealParameterImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealPropertyImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealSubTypeImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealSystemImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealTaskImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealTypeImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealUserImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<RealValueImpl<?>>>() {}));

        // hardwares
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealHardwareImpl.Factory>() {}));

        // nodes
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealNodeImpl.Factory>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealNodeListImpl.Factory>() {}));

        // options
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealOptionImpl.Factory>() {}));

        // parameter
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealParameterImpl.Factory>() {}));

        // property
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealPropertyImpl.Factory>() {}));

        // subtype
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealSubTypeImpl.Factory>() {}));

        // devices
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealSystemImpl.Factory>() {}));

        // tasks
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealTaskImpl.Factory>() {}));

        // value
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealValueImpl.Factory>() {}));

        // users
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealUserImpl.Factory>() {}));
    }
}

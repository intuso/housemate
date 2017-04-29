package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
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
                .build(new TypeLiteral<RealDeviceConnectedImpl.Factory>() {}));

        // lists
        // generated
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealAutomationImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealCommandImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealConditionImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealDeviceConnectedImpl>>() {}));
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
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealDeviceCombiImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealTaskImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealTypeImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealUserImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealValueImpl<?>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListGeneratedImpl.Factory<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>>>() {}));
        // persisted
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<Automation.Data, RealAutomationImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<Condition.Data, RealConditionImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<Device.Connected.Data, RealDeviceConnectedImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<Hardware.Data, RealHardwareImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<Device.Combi.Data, RealDeviceCombiImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<Task.Data, RealTaskImpl>>() {}));
        install(new FactoryModuleBuilder()
                .build(new TypeLiteral<RealListPersistedImpl.Factory<User.Data, RealUserImpl>>() {}));
        // persisted data classes
        bind(new TypeLiteral<Class<Automation.Data>>() {}).toInstance(Automation.Data.class);
        bind(new TypeLiteral<Class<Condition.Data>>() {}).toInstance(Condition.Data.class);
        bind(new TypeLiteral<Class<Device.Connected.Data>>() {}).toInstance(Device.Connected.Data.class);
        bind(new TypeLiteral<Class<Hardware.Data>>() {}).toInstance(Hardware.Data.class);
        bind(new TypeLiteral<Class<Property.Data>>() {}).toInstance(Property.Data.class);
        bind(new TypeLiteral<Class<Device.Combi.Data>>() {}).toInstance(Device.Combi.Data.class);
        bind(new TypeLiteral<Class<Task.Data>>() {}).toInstance(Task.Data.class);
        bind(new TypeLiteral<Class<User.Data>>() {}).toInstance(User.Data.class);
        // persisted element factories
        bind(new TypeLiteral<RealListPersistedImpl.ElementFactory<Automation.Data, RealAutomationImpl>>() {}).to(RealAutomationImpl.LoadPersisted.class);
        bind(new TypeLiteral<RealListPersistedImpl.ElementFactory<Condition.Data, RealConditionImpl>>() {}).to(RealConditionImpl.LoadPersisted.class);
        bind(new TypeLiteral<RealListPersistedImpl.ElementFactory<Device.Connected.Data, RealDeviceConnectedImpl>>() {}).to(RealDeviceConnectedImpl.LoadPersisted.class);
        bind(new TypeLiteral<RealListPersistedImpl.ElementFactory<Hardware.Data, RealHardwareImpl>>() {}).to(RealHardwareImpl.LoadPersisted.class);
        bind(new TypeLiteral<RealListPersistedImpl.ElementFactory<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>>>() {}).to(RealPropertyImpl.LoadPersistedDeviceObjectReference.class);
        bind(new TypeLiteral<RealListPersistedImpl.ElementFactory<Device.Combi.Data, RealDeviceCombiImpl>>() {}).to(RealDeviceCombiImpl.LoadPersisted.class);
        bind(new TypeLiteral<RealListPersistedImpl.ElementFactory<Task.Data, RealTaskImpl>>() {}).to(RealTaskImpl.LoadPersisted.class);
        bind(new TypeLiteral<RealListPersistedImpl.ElementFactory<User.Data, RealUserImpl>>() {}).to(RealUserImpl.LoadPersisted.class);

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
                .build(new TypeLiteral<RealDeviceCombiImpl.Factory>() {}));

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

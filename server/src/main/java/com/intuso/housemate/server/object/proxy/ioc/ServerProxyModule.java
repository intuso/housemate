package com.intuso.housemate.server.object.proxy.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.server.object.proxy.*;
import com.intuso.utilities.object.ObjectFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/01/14
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class ServerProxyModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ApplicationData, ServerProxyApplication>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ApplicationInstanceData, ServerProxyApplicationInstance>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<AutomationData, ServerProxyAutomation>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<CommandData, ServerProxyCommand>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ConditionData, ServerProxyCondition>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<DeviceData, ServerProxyDevice>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<HardwareData, ServerProxyHardware>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ListData<HousemateData<?>>, ServerProxyList<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<OptionData, ServerProxyOption>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ParameterData, ServerProxyParameter>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<PropertyData, ServerProxyProperty>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<SubTypeData, ServerProxySubType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<TaskData, ServerProxyTask>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<TypeData<HousemateData<?>>, ServerProxyType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<UserData, ServerProxyUser>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ValueData, ServerProxyValue>>() {}));
        bind(new TypeLiteral<ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>>() {}).to(ServerProxyFactory.All.class);
        bind(ServerProxyRoot.class); // this binding makes the child injector create this, rather than delegating to the parent injector. Important as the injector that is injected into that class is the one that created it, and it needs to use the child injector.
    }
}

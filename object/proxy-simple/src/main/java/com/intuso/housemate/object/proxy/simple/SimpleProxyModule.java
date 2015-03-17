package com.intuso.housemate.object.proxy.simple;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.ProxyObject;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/01/14
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class SimpleProxyModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ApplicationData, SimpleProxyApplication>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ApplicationInstanceData, SimpleProxyApplicationInstance>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<AutomationData, SimpleProxyAutomation>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<CommandData, SimpleProxyCommand>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ConditionData, SimpleProxyCondition>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<DeviceData, SimpleProxyDevice>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<HardwareData, SimpleProxyHardware>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ListData<HousemateData<?>>, SimpleProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<OptionData, SimpleProxyOption>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ParameterData, SimpleProxyParameter>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<PropertyData, SimpleProxyProperty>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<SubTypeData, SimpleProxySubType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<TaskData, SimpleProxyTask>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<TypeData<HousemateData<?>>, SimpleProxyType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<UserData, SimpleProxyUser>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ValueData, SimpleProxyValue>>() {}));
        bind(new TypeLiteral<HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).to(SimpleProxyFactory.class);
        bind(SimpleProxyRoot.class).in(Scopes.SINGLETON);
    }
}

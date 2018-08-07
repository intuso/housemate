package com.intuso.housemate.client.proxy.internal.object.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.api.internal.object.view.DeviceView;
import com.intuso.housemate.client.proxy.internal.object.*;

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
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyAutomation.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyCommand.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyCondition.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyDeviceComponent.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyDeviceConnected.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyDeviceGroup.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyHardware.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyAutomation.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyCommand.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyCondition.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyDeviceComponent.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyDeviceConnected.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyDeviceGroup.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyHardware.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyNode.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyOption.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyParameter.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyProperty.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyReference.Simple<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?>>>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyServer.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxySubType.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyTask.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyType.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyUser.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyList.Simple<ProxyValue.Simple>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyNode.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyOption.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyParameter.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyProperty.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyReference.Simple<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?>>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyServer.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxySubType.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyTask.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyType.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyUser.Simple>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<ProxyValue.Simple>>() {}));
    }
}

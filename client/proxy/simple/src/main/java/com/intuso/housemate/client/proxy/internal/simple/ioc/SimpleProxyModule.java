package com.intuso.housemate.client.proxy.internal.simple.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyObject;
import com.intuso.housemate.client.proxy.internal.simple.*;

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
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyAutomation>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyCommand>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyCondition>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyConnectedDevice>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxySystem>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyHardware>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyAutomation>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyCommand>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyCondition>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyConnectedDevice>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxySystem>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyHardware>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyNode>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyOption>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyParameter>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyProperty>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyServer>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxySubType>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyTask>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyType>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyUser>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyList<SimpleProxyValue>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyNode>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyOption>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyParameter>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyProperty>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyServer>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxySubType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyTask>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyUser>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObject.Factory<SimpleProxyValue>>() {}));
    }
}

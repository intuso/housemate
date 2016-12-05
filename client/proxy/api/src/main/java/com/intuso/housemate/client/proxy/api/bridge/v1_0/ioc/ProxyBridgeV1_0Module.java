package com.intuso.housemate.client.proxy.api.bridge.v1_0.ioc;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.proxy.api.bridge.ioc.Proxy;
import com.intuso.housemate.client.proxy.api.bridge.v1_0.*;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/01/14
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class ProxyBridgeV1_0Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyAutomationBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyCommandBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyConditionBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyDeviceBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyFeatureBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyHardwareBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyAutomationBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyCommandBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyConditionBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyDeviceBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyFeatureBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyHardwareBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyNodeBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyOptionBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyParameterBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyPropertyBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxySubTypeBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyTaskBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyTypeBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyUserBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyListBridge<ProxyValueBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyNodeBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyOptionBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyParameterBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyPropertyBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxySubTypeBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyTaskBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyTypeBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyUserBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ProxyObjectBridge.Factory<ProxyValueBridge>>() {}));

        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(ProxyServerBridge.Service.class);
    }

    @Provides
    @ProxyV1_0
    public Logger getServerLogger(@Proxy Logger logger) {
        return ChildUtil.logger(logger, "v1_0");
    }
}

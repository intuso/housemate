package com.intuso.housemate.client.real.impl.bridge.v1_0.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.real.impl.bridge.v1_0.*;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/01/14
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class RealBridgeV1_0Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealCommandBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealConnectedDeviceBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealHardwareBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealListBridge<RealCommandBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealListBridge<RealConnectedDeviceBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealListBridge<RealHardwareBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealListBridge<RealOptionBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealListBridge<RealParameterBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealListBridge<RealPropertyBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealListBridge<RealSubTypeBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealListBridge<RealTypeBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealListBridge<RealValueBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealOptionBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealParameterBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealPropertyBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealSubTypeBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealTypeBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<RealObjectBridge.Factory<RealValueBridge>>() {}));

        install(new FactoryModuleBuilder().build(RealNodeBridge.Factory.class));
    }
}

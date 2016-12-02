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
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<CommandBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<HardwareBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<ListBridge<CommandBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<ListBridge<HardwareBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<ListBridge<OptionBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<ListBridge<ParameterBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<ListBridge<PropertyBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<ListBridge<SubTypeBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<ListBridge<TypeBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<ListBridge<ValueBridge>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<OptionBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<ParameterBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<PropertyBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<SubTypeBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<TypeBridge>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<BridgeObject.Factory<ValueBridge>>() {}));

        install(new FactoryModuleBuilder().build(NodeBridge.Factory.class));
    }
}

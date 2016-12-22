package com.intuso.housemate.client.real.api.bridge.v1_0.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.real.api.bridge.v1_0.*;

/**
 * Created by tomc on 06/11/15.
 */
public class RealApiBridgeV1_0Module extends AbstractModule {
    @Override
    protected void configure() {

        // setup assisted inject factories
        install(new FactoryModuleBuilder().build(RealCommandBridge.Factory.class));
        install(new FactoryModuleBuilder().build(RealCommandBridgeReverse.Factory.class));
        install(new FactoryModuleBuilder().build(RealDeviceBridge.Factory.class));
        install(new FactoryModuleBuilder().build(RealDeviceBridgeReverse.Factory.class));
        install(new FactoryModuleBuilder().build(RealFeatureBridge.Factory.class));
        install(new FactoryModuleBuilder().build(RealFeatureBridgeReverse.Factory.class));
        install(new FactoryModuleBuilder().build(RealPropertyBridge.Factory.class));
        install(new FactoryModuleBuilder().build(RealPropertyBridgeReverse.Factory.class));
        install(new FactoryModuleBuilder().build(RealTypeBridge.Factory.class));
        install(new FactoryModuleBuilder().build(RealTypeBridgeReverse.Factory.class));
        install(new FactoryModuleBuilder().build(RealValueBridge.Factory.class));
        install(new FactoryModuleBuilder().build(RealValueBridgeReverse.Factory.class));
    }
}

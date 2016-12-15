package com.intuso.housemate.plugin.api.bridge.v1_0.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.*;

/**
 * Created by tomc on 15/12/16.
 */
public class PluginApiBridgeV1_0Module extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(ConditionDriverFactoryBridge.Factory.class));
        install(new FactoryModuleBuilder().build(ConditionDriverFactoryBridgeReverse.Factory.class));
        install(new FactoryModuleBuilder().build(FeatureDriverFactoryBridge.Factory.class));
        install(new FactoryModuleBuilder().build(FeatureDriverFactoryBridgeReverse.Factory.class));
        install(new FactoryModuleBuilder().build(HardwareDriverFactoryBridge.Factory.class));
        install(new FactoryModuleBuilder().build(HardwareDriverFactoryBridgeReverse.Factory.class));
        install(new FactoryModuleBuilder().build(TaskDriverFactoryBridge.Factory.class));
        install(new FactoryModuleBuilder().build(TaskDriverFactoryBridgeReverse.Factory.class));
    }
}

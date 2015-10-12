package com.intuso.housemate.comms.api.bridge.v1_0.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.comms.api.bridge.v1_0.ReceiverBridgeReverse;
import com.intuso.housemate.comms.api.bridge.v1_0.RouterListenerBridgeReverse;
import com.intuso.housemate.comms.api.bridge.v1_0.RouterV1_0Bridge;
import com.intuso.housemate.comms.v1_0.api.Router;

/**
 * Created by tomc on 25/09/15.
 */
public class CommsAPIV1_0BridgeModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new Key<Router<?>>() {}).to(RouterV1_0Bridge.class);
        install(new FactoryModuleBuilder().build(ReceiverBridgeReverse.Factory.class));
        install(new FactoryModuleBuilder().build(RouterListenerBridgeReverse.Factory.class));
    }
}

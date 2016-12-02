package com.intuso.housemate.client.real.impl.bridge.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.client.real.impl.bridge.v1_0.ioc.RealBridgeV1_0Module;

/**
 * Created by tomc on 02/12/16.
 */
public class RealBridgeModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new RealBridgeV1_0Module());
    }
}

package com.intuso.housemate.client.real.api.bridge.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.client.real.api.bridge.v1_0.ioc.RealApiBridgeV1_0Module;

/**
 * Created by tomc on 02/12/16.
 */
public class RealApiBridgeModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new RealApiBridgeV1_0Module());
    }
}

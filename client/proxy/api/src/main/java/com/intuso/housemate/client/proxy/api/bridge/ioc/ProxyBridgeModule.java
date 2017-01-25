package com.intuso.housemate.client.proxy.api.bridge.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.client.proxy.api.bridge.v1_0.ioc.ProxyBridgeV1_0Module;

/**
 * Created by tomc on 05/12/16.
 */
public class ProxyBridgeModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ProxyBridgeV1_0Module());
    }
}

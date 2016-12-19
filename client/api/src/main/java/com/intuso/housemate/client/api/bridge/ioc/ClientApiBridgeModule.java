package com.intuso.housemate.client.api.bridge.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.client.api.bridge.v1_0.ioc.ClientApiV1_0BridgeModule;

/**
 * Created by tomc on 29/09/15.
 */
public class ClientApiBridgeModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ClientApiV1_0BridgeModule());
    }
}

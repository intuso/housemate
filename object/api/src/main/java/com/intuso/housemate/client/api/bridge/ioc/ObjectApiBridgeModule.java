package com.intuso.housemate.client.api.bridge.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.client.api.bridge.v1_0.ioc.ObjectApiV1_0BridgeModule;

/**
 * Created by tomc on 29/09/15.
 */
public class ObjectApiBridgeModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ObjectApiV1_0BridgeModule());
    }
}

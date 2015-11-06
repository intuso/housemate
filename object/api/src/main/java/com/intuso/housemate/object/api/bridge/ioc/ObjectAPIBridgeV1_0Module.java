package com.intuso.housemate.object.api.bridge.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.object.api.bridge.v1_0.ioc.ObjectAPIV1_0BridgeModule;

/**
 * Created by tomc on 29/09/15.
 */
public class ObjectAPIBridgeV1_0Module extends AbstractModule {
    @Override
    protected void configure() {
        install(new ObjectAPIV1_0BridgeModule());
    }
}

package com.intuso.housemate.comms.api.bridge.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.comms.api.bridge.v1_0.ioc.CommsAPIV1_0BridgeModule;

/**
 * Created by tomc on 25/09/15.
 */
public class CommsAPIBridgeV1_0Module extends AbstractModule {
    @Override
    protected void configure() {
        install(new CommsAPIV1_0BridgeModule());
    }
}

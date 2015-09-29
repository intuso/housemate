package com.intuso.housemate.comms.api.bridge.v1_0.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.comms.api.bridge.v1_0.RouterV1_0Bridge;
import com.intuso.housemate.comms.v1_0.api.Router;

/**
 * Created by tomc on 25/09/15.
 */
public class CommsAPIV1_0BridgeModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Router.class).to(RouterV1_0Bridge.class);
    }
}

package com.intuso.housemate.webserver.api.server.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.webserver.api.server.v1_0.ioc.ServerV1_0Module;

/**
 * Created by tomc on 21/01/17.
 */
public class ServerApiModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ServerV1_0Module());
    }
}

package com.intuso.housemate.webserver.api.ioc;

import com.google.inject.servlet.ServletModule;
import com.intuso.housemate.webserver.api.server.ioc.ServerApiModule;

/**
 * Created by tomc on 21/01/17.
 */
public class ApiModule extends ServletModule {
    @Override
    protected void configureServlets() {
        install(new ServerApiModule());
    }
}

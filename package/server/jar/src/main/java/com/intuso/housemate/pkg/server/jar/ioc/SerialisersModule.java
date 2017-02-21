package com.intuso.housemate.pkg.server.jar.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.client.v1_0.serialisation.json.ioc.JsonSerialiserServerModule;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 04/01/14
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
public class SerialisersModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JsonSerialiserServerModule());
    }
}

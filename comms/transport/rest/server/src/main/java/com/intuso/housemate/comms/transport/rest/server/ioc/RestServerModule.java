package com.intuso.housemate.comms.transport.rest.server.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.comms.transport.rest.server.RestServer;
import com.intuso.housemate.plugin.api.internal.ExternalClientRouter;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 04/01/14
 * Time: 10:16
 * To change this template use File | Settings | File Templates.
 */
public class RestServerModule extends AbstractModule {

    public RestServerModule(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(RestServer.PORT, "46872");
    }

    @Override
    protected void configure() {
        bind(RestServer.class).in(Scopes.SINGLETON);
        Multibinder.newSetBinder(binder(), ExternalClientRouter.class).addBinding().to(RestServer.class);
    }
}

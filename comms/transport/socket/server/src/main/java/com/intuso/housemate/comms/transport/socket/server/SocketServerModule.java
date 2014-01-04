package com.intuso.housemate.comms.transport.socket.server;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.plugin.api.ExternalClientRouter;
import com.intuso.utilities.properties.api.PropertyContainer;
import com.intuso.utilities.properties.api.PropertyValue;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 04/01/14
 * Time: 10:16
 * To change this template use File | Settings | File Templates.
 */
public class SocketServerModule extends AbstractModule {

    public SocketServerModule(PropertyContainer properties) {
        properties.set(SocketServer.PORT, new PropertyValue("default", 0, "46873"));
    }

    @Override
    protected void configure() {
        bind(SocketServer.class).in(Scopes.SINGLETON);
        Multibinder.newSetBinder(binder(), ExternalClientRouter.class).addBinding().to(SocketServer.class);
    }
}

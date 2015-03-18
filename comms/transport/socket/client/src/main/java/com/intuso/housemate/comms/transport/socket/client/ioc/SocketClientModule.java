package com.intuso.housemate.comms.transport.socket.client.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.comms.transport.socket.client.SocketClient;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 19:12
 * To change this template use File | Settings | File Templates.
 */
public class SocketClientModule extends AbstractModule {

    public SocketClientModule(PropertyRepository defaultProperties) {
        defaultProperties.set(SocketClient.HOST, "localhost");
        defaultProperties.set(SocketClient.PORT, "46873");
    }

    @Override
    protected void configure() {
        bind(SocketClient.class).in(Scopes.SINGLETON);
        bind(Router.class).to(SocketClient.class);
    }
}

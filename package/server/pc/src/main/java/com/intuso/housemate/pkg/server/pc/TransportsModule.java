package com.intuso.housemate.pkg.server.pc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.comms.transport.rest.RestServerModule;
import com.intuso.housemate.comms.transport.socket.server.SocketServerModule;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 04/01/14
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
public class TransportsModule extends AbstractModule {

    private final PropertyContainer properties;

    public TransportsModule(PropertyContainer properties) {
        this.properties = properties;
    }

    @Override
    protected void configure() {
        install(new SocketServerModule(properties));
        install(new RestServerModule(properties));
    }
}

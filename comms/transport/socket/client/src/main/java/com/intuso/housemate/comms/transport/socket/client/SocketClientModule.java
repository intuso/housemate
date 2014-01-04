package com.intuso.housemate.comms.transport.socket.client;

import com.google.inject.AbstractModule;
import com.intuso.housemate.api.comms.Router;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 19:12
 * To change this template use File | Settings | File Templates.
 */
public class SocketClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Router.class).to(SocketClient.class);
    }
}

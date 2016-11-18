package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.plugin.host.internal.PluginHost;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    public final static String SERVER_NAME = "server.name";

    private final Injector injector;

    @Inject
    public Server(Injector injector) {
        this.injector = injector;
    }

    public final void start() {
        // getting an instance is enough
        injector.getInstance(PluginHost.class);
    }

    public Injector getInjector() {
        return injector;
    }
}

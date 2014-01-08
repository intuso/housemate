package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.object.bridge.RootObjectBridge;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.housemate.server.plugin.main.MainPluginModule;

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
        // start the main router
        injector.getInstance(MainRouter.class).start();

        // make sure all the framework is created before we add plugins
        injector.getInstance(RootObjectBridge.class);

        // add the default plugin
        injector.getInstance(PluginManager.class).addPlugin(MainPluginModule.class);
    }

    public final void stop() {
        // stop the main router
        injector.getInstance(MainRouter.class).stop();
    }

    public Injector getInjector() {
        return injector;
    }
}

package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;
import com.intuso.housemate.plugin.manager.PluginManager;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.plugin.main.ioc.MainPluginModule;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    public final static String INSTANCE_ID = UUID.randomUUID().toString();
    public final static ApplicationDetails INTERNAL_APPLICATION_DETAILS = new ApplicationDetails("local", "Local", "Local");
    public final static com.intuso.housemate.comms.api.internal.access.ApplicationDetails INTERNAL_APPLICATION_DETAILS_V1_0 = new com.intuso.housemate.comms.api.internal.access.ApplicationDetails("local", "Local", "Local");

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
        injector.getInstance(RootBridge.class);

        // add the default plugin
        injector.getInstance(PluginManager.class).addPlugin(MainPluginModule.class);
    }

    public final void acceptClients() {
        injector.getInstance(MainRouter.class).startExternalRouters();
    }

    public final void stop() {
        // stop the main router
        injector.getInstance(MainRouter.class).stop();
    }

    public Injector getInjector() {
        return injector;
    }
}

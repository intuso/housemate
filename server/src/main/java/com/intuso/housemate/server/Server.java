package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.object.server.proxy.ServerProxyFactory;
import com.intuso.housemate.object.server.proxy.ServerProxyRootObject;
import com.intuso.housemate.object.server.real.ServerRealRootObject;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.object.ServerProxyResourcesImpl;
import com.intuso.housemate.server.object.ServerRealResourcesImpl;
import com.intuso.housemate.server.object.bridge.RootObjectBridge;
import com.intuso.housemate.server.object.bridge.ServerBridgeResources;

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
        // set root objects
        injector.getInstance(ServerRealResourcesImpl.class).setRoot(injector.getInstance(ServerRealRootObject.class));
        injector.getInstance(new Key<ServerProxyResourcesImpl<ServerProxyFactory.All>>() {}).setRoot(injector.getInstance(ServerProxyRootObject.class));
        injector.getInstance(ServerBridgeResources.class).setRoot(injector.getInstance(RootObjectBridge.class));

        // start the main router
        injector.getInstance(MainRouter.class).start();
    }

    public final void stop() {
        injector.getInstance(MainRouter.class).stop();
    }

    public Injector getInjector() {
        return injector;
    }
}

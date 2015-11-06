package com.intuso.housemate.server.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.intuso.housemate.client.real.api.bridge.v1_0.ioc.ClientRealAPIBridgeV1_0Module;
import com.intuso.housemate.client.real.api.internal.RealRoot;
import com.intuso.housemate.client.real.impl.internal.ioc.RealObjectModule;
import com.intuso.housemate.comms.api.bridge.ioc.CommsAPIBridgeV1_0Module;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.object.api.bridge.ioc.ObjectAPIBridgeV1_0Module;
import com.intuso.housemate.object.api.internal.ObjectRoot;
import com.intuso.housemate.object.api.internal.Root;
import com.intuso.housemate.plugin.manager.ioc.PluginHostModule;
import com.intuso.housemate.server.Server;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.comms.RemoteClientManager;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.object.general.ServerGeneralRoot;
import com.intuso.housemate.server.object.real.FactoryPluginListener;
import com.intuso.housemate.server.object.real.ServerRealRoot;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 */
public class ServerModule extends AbstractModule {

    public ServerModule(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(Server.SERVER_NAME, "My Server");
    }

    @Override
    protected void configure() {

        // install api bridge modules
        install(new ObjectAPIBridgeV1_0Module());
        install(new CommsAPIBridgeV1_0Module());
        install(new ClientRealAPIBridgeV1_0Module());

        // install plugin modules
        install(new RealObjectModule());
        install(new PluginHostModule());

        // bind everything as singletons that should be
        // root objects
        bind(ServerRealRoot.class).in(Scopes.SINGLETON);
        bind(ServerGeneralRoot.class).in(Scopes.SINGLETON);
        bind(RootBridge.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<ObjectRoot<?, ?>>() {}).to(RootBridge.class);
        // other things
        bind(FactoryPluginListener.class).in(Scopes.SINGLETON);
        bind(Server.class).in(Scopes.SINGLETON);
        bind(RemoteClientManager.class).in(Scopes.SINGLETON);
        bind(MainRouter.class).in(Scopes.SINGLETON);

        // bind implementations
        bind(ObjectRoot.class).to(RootBridge.class);
        bind(RealRoot.class).to(ServerRealRoot.class);
        bind(new Key<Router<?>>() {}).to(MainRouter.class);
        bind(new Key<Root<?, ?>>() {}).to(RootBridge.class);
    }
}

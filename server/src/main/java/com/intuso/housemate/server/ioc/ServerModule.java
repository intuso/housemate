package com.intuso.housemate.server.ioc;

import com.google.inject.*;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.root.ObjectRoot;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.object.server.ServerProxyType;
import com.intuso.housemate.plugin.host.ioc.PluginHostModule;
import com.intuso.housemate.realclient.ioc.RealClientModule;
import com.intuso.housemate.server.Server;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.comms.RemoteClientManager;
import com.intuso.housemate.server.object.bridge.ListBridge;
import com.intuso.housemate.server.object.bridge.MultiListBridge;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.object.bridge.TypeBridge;
import com.intuso.housemate.server.object.general.ServerGeneralRoot;
import com.intuso.housemate.server.object.real.ServerRealRoot;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 */
public class ServerModule extends AbstractModule {

    public ServerModule(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(Server.SERVER_NAME, "My Server");
    }

    @Override
    protected void configure() {

        install(new RealClientModule());
        install(new PluginHostModule());

        // bind everything as singletons that should be
        // root objects
        bind(RealRoot.class).to(ServerRealRoot.class);
        bind(ServerRealRoot.class).in(Scopes.SINGLETON);
        bind(ServerGeneralRoot.class).in(Scopes.SINGLETON);
        bind(RootBridge.class).in(Scopes.SINGLETON);

        // factories
        // other things
        bind(Server.class).in(Scopes.SINGLETON);
        bind(RemoteClientManager.class).in(Scopes.SINGLETON);
        bind(MainRouter.class).in(Scopes.SINGLETON);

        // bind implementations
        bind(Router.class).to(MainRouter.class);
        bind(new TypeLiteral<Root<?>>() {}).to(RootBridge.class);
        bind(new TypeLiteral<ListBridge<TypeData<?>, ServerProxyType, TypeBridge>>() {})
                .to(new TypeLiteral<MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge>>() {});
    }

    @Provides
    @Singleton
    public MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge> getBridgeTypes(Log log, ListenersFactory listenersFactory) {
        MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge> typeList = new MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge>(log, listenersFactory,
                new ListData<TypeData<?>>(ObjectRoot.TYPES_ID, "Types", "Types"));
        typeList.setConverter(new TypeBridge.Converter(log, listenersFactory, typeList));
        return typeList;
    }
}

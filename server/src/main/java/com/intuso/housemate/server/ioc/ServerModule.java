package com.intuso.housemate.server.ioc;

import com.google.inject.*;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.housemate.object.server.real.ServerRealRoot;
import com.intuso.housemate.plugin.host.ioc.PluginHostModule;
import com.intuso.housemate.realclient.ioc.RealClientModule;
import com.intuso.housemate.server.Server;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.comms.RemoteClientManager;
import com.intuso.housemate.server.factory.ConditionFactory;
import com.intuso.housemate.server.factory.TaskFactory;
import com.intuso.housemate.server.object.LifecycleHandlerImpl;
import com.intuso.housemate.server.object.bridge.ListBridge;
import com.intuso.housemate.server.object.bridge.MultiListBridge;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.object.bridge.TypeBridge;
import com.intuso.housemate.server.object.general.ServerGeneralRoot;
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
        bind(ServerRealRoot.class).in(Scopes.SINGLETON);
        bind(ServerGeneralRoot.class).in(Scopes.SINGLETON);
        bind(RootBridge.class).in(Scopes.SINGLETON);

        // factories
        bind(ConditionFactory.class).in(Scopes.SINGLETON);
        bind(TaskFactory.class).in(Scopes.SINGLETON);
        // other things
        bind(Server.class).in(Scopes.SINGLETON);
        bind(RemoteClientManager.class).in(Scopes.SINGLETON);
        bind(MainRouter.class).in(Scopes.SINGLETON);
        bind(LifecycleHandlerImpl.class).in(Scopes.SINGLETON);

        // bind implementations
        bind(Router.class).to(MainRouter.class);
        bind(LifecycleHandler.class).to(LifecycleHandlerImpl.class);
        bind(new TypeLiteral<Root<?>>() {}).to(RootBridge.class);
        bind(new TypeLiteral<ListBridge<TypeData<?>, ServerProxyType, TypeBridge>>() {})
                .to(new TypeLiteral<MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge>>() {});
    }

    @Provides
    @Singleton
    public MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge> getBridgeTypes(Log log, ListenersFactory listenersFactory) {
        MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge> typeList = new MultiListBridge<TypeData<?>, ServerProxyType, TypeBridge>(log, listenersFactory,
                new ListData<TypeData<?>>(Root.TYPES_ID, "Types", "Types"));
        typeList.setConverter(new TypeBridge.Converter(log, listenersFactory, typeList));
        return typeList;
    }
}

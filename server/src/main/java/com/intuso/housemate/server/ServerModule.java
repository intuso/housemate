package com.intuso.housemate.server;

import com.google.inject.*;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.impl.type.*;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.proxy.ServerProxyFactory;
import com.intuso.housemate.object.server.proxy.ServerProxyResources;
import com.intuso.housemate.object.server.proxy.ServerProxyRootObject;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.object.server.real.ServerRealRootObject;
import com.intuso.housemate.server.client.LocalClient;
import com.intuso.housemate.server.client.LocalClientRoot;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.comms.RemoteClientManager;
import com.intuso.housemate.server.factory.ConditionFactory;
import com.intuso.housemate.server.factory.DeviceFactory;
import com.intuso.housemate.server.factory.TaskFactory;
import com.intuso.housemate.server.object.LifecycleHandlerImpl;
import com.intuso.housemate.server.object.ServerProxyResourcesImpl;
import com.intuso.housemate.server.object.ServerRealResourcesImpl;
import com.intuso.housemate.server.object.bridge.ListBridge;
import com.intuso.housemate.server.object.bridge.RootObjectBridge;
import com.intuso.housemate.server.object.bridge.ServerBridgeResources;
import com.intuso.housemate.server.object.bridge.TypeBridge;
import com.intuso.housemate.server.object.general.ServerGeneralRootObject;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;
import com.intuso.utilities.properties.api.PropertyValue;

/**
 */
public class ServerModule extends AbstractModule {

    public ServerModule(PropertyContainer properties) {
        properties.set(Server.SERVER_NAME, new PropertyValue("default", 0, "My Server"));
    }

    @Override
    protected void configure() {

        // bind everything as singletons that should be
        // root objects
        bind(ServerProxyRootObject.class).in(Scopes.SINGLETON);
        bind(ServerRealRootObject.class).in(Scopes.SINGLETON);
        bind(ServerGeneralRootObject.class).in(Scopes.SINGLETON);
        bind(RootObjectBridge.class).in(Scopes.SINGLETON);
        bind(RealRootObject.class).in(Scopes.SINGLETON);
        bind(LocalClientRoot.class).in(Scopes.SINGLETON);
        // common types
        bind(BooleanType.class).in(Scopes.SINGLETON);
        bind(DaysType.class).in(Scopes.SINGLETON);
        bind(DoubleType.class).in(Scopes.SINGLETON);
        bind(IntegerType.class).in(Scopes.SINGLETON);
        bind(RealObjectType.class).in(Scopes.SINGLETON);
        bind(StringType.class).in(Scopes.SINGLETON);
        bind(TimeType.class).in(Scopes.SINGLETON);
        bind(TimeUnitType.class).in(Scopes.SINGLETON);
        // factories
        bind(ConditionFactory.class).in(Scopes.SINGLETON);
        bind(DeviceFactory.class).in(Scopes.SINGLETON);
        bind(TaskFactory.class).in(Scopes.SINGLETON);
        // resources
        bind(ResourcesImpl.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<ServerProxyResourcesImpl<ServerProxyFactory.All>>() {}).in(Scopes.SINGLETON);
        bind(ServerBridgeResources.class).in(Scopes.SINGLETON);
        bind(ServerRealResourcesImpl.class).in(Scopes.SINGLETON);
        bind(RealResources.class).in(Scopes.SINGLETON);
        // other things
        bind(Server.class).in(Scopes.SINGLETON);
        bind(LocalClient.class).in(Scopes.SINGLETON);
        bind(RemoteClientManager.class).in(Scopes.SINGLETON);
        bind(PluginManager.class).in(Scopes.SINGLETON);
        bind(MainRouter.class).in(Scopes.SINGLETON);
        bind(LifecycleHandlerImpl.class).in(Scopes.SINGLETON);

        // bind implementations
        bind(Resources.class).to(ResourcesImpl.class);
        bind(Router.class).to(MainRouter.class);
        bind(ServerRealResources.class).to(ServerRealResourcesImpl.class);
        bind(new TypeLiteral<ServerProxyResources<ServerProxyFactory.All>>() {}).to(new TypeLiteral<ServerProxyResourcesImpl<ServerProxyFactory.All>>() {
        });
        bind(LifecycleHandler.class).to(LifecycleHandlerImpl.class);
        bind(new TypeLiteral<Root<?>>() {}).to(RootObjectBridge.class);
    }

    @Provides
    @Singleton
    public RealList<TypeData<?>, RealType<?, ?, ?>> getRealTypes(RealResources resources) {
        return new RealList<TypeData<?>, RealType<?, ?, ?>>(resources, Root.TYPES_ID, "Types", "Types");
    }

    @Provides
    @Singleton
    public RealList<DeviceData, RealDevice> getRealDevices(LocalClientRoot root) {
        return root.getDevices();
    }

    @Provides
    @Singleton
    public ListBridge<TypeData<?>, ServerProxyType, TypeBridge> getBridgeTypes(ServerBridgeResources resources,
                                                                               ServerProxyRootObject proxyRoot) {
        ListBridge<TypeData<?>, ServerProxyType, TypeBridge> typeList =
                new ListBridge<TypeData<?>, ServerProxyType, TypeBridge>(resources, proxyRoot.getTypes());
        typeList.convert(new TypeBridge.Converter(resources, typeList));
        return typeList;
    }

    private static class ResourcesImpl implements Resources {

        private final Log log;
        private final PropertyContainer properties;

        @Inject
        private ResourcesImpl(Log log, PropertyContainer properties) {
            this.log = log;
            this.properties = properties;
        }

        @Override
        public Log getLog() {
            return log;
        }

        @Override
        public PropertyContainer getProperties() {
            return properties;
        }
    }
}

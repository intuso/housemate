package com.intuso.housemate.server;

import com.google.inject.*;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.proxy.ServerProxyFactory;
import com.intuso.housemate.object.server.proxy.ServerProxyResources;
import com.intuso.housemate.object.server.proxy.ServerProxyRootObject;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.server.client.LocalClientRoot;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.object.LifecycleHandlerImpl;
import com.intuso.housemate.server.object.ServerProxyResourcesImpl;
import com.intuso.housemate.server.object.ServerRealResourcesImpl;
import com.intuso.housemate.server.object.bridge.ListBridge;
import com.intuso.housemate.server.object.bridge.RootObjectBridge;
import com.intuso.housemate.server.object.bridge.ServerBridgeResources;
import com.intuso.housemate.server.object.bridge.TypeBridge;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 */
public class ServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Resources.class).to(ResourcesImpl.class);
        bind(Router.class).to(MainRouter.class);
        bind(ServerRealResources.class).to(ServerRealResourcesImpl.class);
        bind(new TypeLiteral<ServerProxyResources<ServerProxyFactory.All>>() {}).to(new TypeLiteral<ServerProxyResourcesImpl<ServerProxyFactory.All>>() {});
        bind(LifecycleHandler.class).to(LifecycleHandlerImpl.class);
        bind(new TypeLiteral<Root<?>>() {}).to(RootObjectBridge.class).in(Scopes.SINGLETON);
    }

    @Provides
    public RealResources getRealResources(Log log, PropertyContainer properties, Router router) {
        return new RealResources(log, properties, router);
    }

    @Provides
    @Singleton
    public RealList<TypeData<?>, RealType<?, ?, ?>> getRealTypes(RealResources resources) {
        return new RealList<TypeData<?>, RealType<?, ?, ?>>(resources, Root.TYPES_ID, "Types", "Types");
    }

    @Provides
    public RealList<DeviceData, RealDevice> getRealDevices(LocalClientRoot root) {
        return root.getDevices();
    }

    @Provides
    public ListBridge<TypeData<?>, ServerProxyType, TypeBridge> getBridgeTypes(ServerBridgeResources resources,
                                                                               ServerProxyRootObject proxyRoot) {
        ListBridge<TypeData<?>, ServerProxyType, TypeBridge> typeList =
                new ListBridge<TypeData<?>, ServerProxyType, TypeBridge>(resources, proxyRoot.getTypes());
        typeList.convert(new TypeBridge.Converter(resources, typeList));
        return typeList;
    }

    @Singleton
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

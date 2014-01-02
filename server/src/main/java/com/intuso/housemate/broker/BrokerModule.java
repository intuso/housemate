package com.intuso.housemate.broker;

import com.google.inject.*;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.broker.client.LocalClientRoot;
import com.intuso.housemate.broker.comms.MainRouter;
import com.intuso.housemate.broker.object.BrokerProxyResourcesImpl;
import com.intuso.housemate.broker.object.BrokerRealResourcesImpl;
import com.intuso.housemate.broker.object.LifecycleHandlerImpl;
import com.intuso.housemate.broker.object.bridge.BrokerBridgeResources;
import com.intuso.housemate.broker.object.bridge.ListBridge;
import com.intuso.housemate.broker.object.bridge.RootObjectBridge;
import com.intuso.housemate.broker.object.bridge.TypeBridge;
import com.intuso.housemate.object.broker.LifecycleHandler;
import com.intuso.housemate.object.broker.proxy.BrokerProxyFactory;
import com.intuso.housemate.object.broker.proxy.BrokerProxyResources;
import com.intuso.housemate.object.broker.proxy.BrokerProxyRootObject;
import com.intuso.housemate.object.broker.proxy.BrokerProxyType;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 */
public class BrokerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Resources.class).to(ResourcesImpl.class);
        bind(Router.class).to(MainRouter.class);
        bind(BrokerRealResources.class).to(BrokerRealResourcesImpl.class);
        bind(new TypeLiteral<BrokerProxyResources<BrokerProxyFactory.All>>() {}).to(new TypeLiteral<BrokerProxyResourcesImpl<BrokerProxyFactory.All>>() {});
        bind(LifecycleHandler.class).to(LifecycleHandlerImpl.class);
        bind(new TypeLiteral<Root<?>>() {}).to(RootObjectBridge.class).in(Scopes.SINGLETON);
    }

    @Provides
    public RealResources getRealResources(Log log, Map<String, String> properties, Router router) {
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
    public ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> getBridgeTypes(BrokerBridgeResources resources,
                                                                               BrokerProxyRootObject proxyRoot) {
        ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> typeList =
                new ListBridge<TypeData<?>, BrokerProxyType, TypeBridge>(resources, proxyRoot.getTypes());
        typeList.convert(new TypeBridge.Converter(resources, typeList));
        return typeList;
    }

    @Singleton
    private static class ResourcesImpl implements Resources {

        private final Log log;
        private final Map<String, String> properties;

        @Inject
        private ResourcesImpl(Log log, Map<String, String> properties) {
            this.log = log;
            this.properties = properties;
        }

        @Override
        public Log getLog() {
            return log;
        }

        @Override
        public Map<String, String> getProperties() {
            return properties;
        }
    }
}

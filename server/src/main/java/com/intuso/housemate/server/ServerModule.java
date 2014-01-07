package com.intuso.housemate.server;

import com.google.inject.*;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealRootObject;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.*;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.proxy.ServerProxyList;
import com.intuso.housemate.object.server.proxy.ServerProxyModule;
import com.intuso.housemate.object.server.proxy.ServerProxyRootObject;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.housemate.object.server.real.ServerRealRootObject;
import com.intuso.housemate.server.client.LocalClient;
import com.intuso.housemate.server.client.LocalClientRoot;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.comms.RemoteClientManager;
import com.intuso.housemate.server.factory.ConditionFactory;
import com.intuso.housemate.server.factory.DeviceFactory;
import com.intuso.housemate.server.factory.TaskFactory;
import com.intuso.housemate.server.object.LifecycleHandlerImpl;
import com.intuso.housemate.server.object.bridge.ListBridge;
import com.intuso.housemate.server.object.bridge.RootObjectBridge;
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

        // install all required modules
        install(new ServerProxyModule());

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
        // other things
        bind(Server.class).in(Scopes.SINGLETON);
        bind(LocalClient.class).in(Scopes.SINGLETON);
        bind(RemoteClientManager.class).in(Scopes.SINGLETON);
        bind(PluginManager.class).in(Scopes.SINGLETON);
        bind(MainRouter.class).in(Scopes.SINGLETON);
        bind(LifecycleHandlerImpl.class).in(Scopes.SINGLETON);

        // bind implementations
        bind(Router.class).to(MainRouter.class);
        bind(LifecycleHandler.class).to(LifecycleHandlerImpl.class);
        bind(new TypeLiteral<Root<?>>() {}).to(RootObjectBridge.class);
    }

    @Provides
    @Singleton
    public RealList<TypeData<?>, RealType<?, ?, ?>> getRealTypes(Log log) {
        return new RealList<TypeData<?>, RealType<?, ?, ?>>(log, Root.TYPES_ID, "Types", "Types");
    }

    @Provides
    @Singleton
    public ServerProxyList<TypeData<?>, ServerProxyType> getServerProxyTypes(Log log, Injector injector) {
        return new ServerProxyList<TypeData<?>, ServerProxyType>(log, injector, new ListData<TypeData<?>>(Root.TYPES_ID, Root.TYPES_ID, "Proxied types"));
    }

    @Provides
    @Singleton
    public ListBridge<TypeData<?>, ServerProxyType, TypeBridge> getBridgeTypes(Log log, ServerProxyRootObject proxyRoot) {
        ListBridge<TypeData<?>, ServerProxyType, TypeBridge> typeList =
                new ListBridge<TypeData<?>, ServerProxyType, TypeBridge>(log, proxyRoot.getTypes());
        typeList.convert(new TypeBridge.Converter(log, typeList));
        return typeList;
    }

    @Provides
    @Singleton
    public RealList<DeviceData, RealDevice> getRealDevices(Log log) {
        return new RealList<DeviceData, RealDevice>(log, Root.DEVICES_ID, "Devices", "Devices");
    }
}

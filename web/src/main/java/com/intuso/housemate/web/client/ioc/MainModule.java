package com.intuso.housemate.web.client.ioc;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.housemate.object.v1_0.api.RegexMatcher;
import com.intuso.housemate.web.client.GWTListenersFactory;
import com.intuso.housemate.web.client.GWTPropertyRepository;
import com.intuso.housemate.web.client.GWTRegexMatcherFactory;
import com.intuso.housemate.web.client.activity.HousemateActivityMapper;
import com.intuso.housemate.web.client.comms.GWTRouter;
import com.intuso.housemate.web.client.comms.LoginManager;
import com.intuso.housemate.web.client.object.*;
import com.intuso.housemate.web.client.place.HousematePlaceController;
import com.intuso.housemate.web.client.place.HousematePlaceHistoryHandler;
import com.intuso.housemate.web.client.place.HousematePlaceHistoryMapper;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.object.ObjectFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.slf4j.Logger;

/**
 */
public class MainModule extends AbstractGinModule {

    @Override
    protected void configure() {

        // GWT stuff
        // main things
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        // activity/place stuff
        bind(ActivityMapper.class).to(HousemateActivityMapper.class);
        bind(PlaceHistoryMapper.class).to(HousematePlaceHistoryMapper.class);
        bind(PlaceHistoryHandler.class).to(HousematePlaceHistoryHandler.class);
        bind(PlaceController.class).to(HousematePlaceController.class);

        // HM stuff
        // object factories
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ApplicationData, GWTProxyApplication>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ApplicationInstanceData, GWTProxyApplicationInstance>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<AutomationData, GWTProxyAutomation>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<CommandData, GWTProxyCommand>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ConditionData, GWTProxyCondition>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<DeviceData, GWTProxyDevice>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<FeatureData, GWTProxyFeature>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<HardwareData, GWTProxyHardware>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ListData<HousemateData<?>>, GWTProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<OptionData, GWTProxyOption>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ParameterData, GWTProxyParameter>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<PropertyData, GWTProxyProperty>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ServerData, GWTProxyServer>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<SubTypeData, GWTProxySubType>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<TaskData, GWTProxyTask>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<TypeData<HousemateData<?>>, GWTProxyType>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<UserData, GWTProxyUser>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<ObjectFactory<ValueData, GWTProxyValue>>() {}));
        bind(new TypeLiteral<ObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).to(GWTProxyFactory.class);
        // other bits
        bind(LoginManager.class).in(Singleton.class);
        bind(GWTProxyRoot.class).in(Singleton.class);
        bind(PropertyRepository.class).to(GWTPropertyRepository.class).in(Singleton.class);
        bind(Logger.class).toProvider(LoggerProvider.class).in(Singleton.class);
        bind(GWTRouter.class).in(Singleton.class);
        bind(new Key<Router<?>>() {}).to(GWTRouter.class);
        bind(RegexMatcher.Factory.class).to(GWTRegexMatcherFactory.class);
        bind(ListenersFactory.class).to(GWTListenersFactory.class);
    }
}

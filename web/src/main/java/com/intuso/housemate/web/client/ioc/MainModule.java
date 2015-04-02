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
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.realclient.RealClientData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.api.resources.RegexMatcherFactory;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.device.feature.ProxyFeatureFactory;
import com.intuso.housemate.web.client.GWTListenersFactory;
import com.intuso.housemate.web.client.GWTLog;
import com.intuso.housemate.web.client.GWTPropertyRepository;
import com.intuso.housemate.web.client.GWTRegexMatcherFactory;
import com.intuso.housemate.web.client.activity.HousemateActivityMapper;
import com.intuso.housemate.web.client.comms.GWTRouter;
import com.intuso.housemate.web.client.comms.LoginManager;
import com.intuso.housemate.web.client.object.*;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeatureFactory;
import com.intuso.housemate.web.client.place.HousematePlaceController;
import com.intuso.housemate.web.client.place.HousematePlaceHistoryHandler;
import com.intuso.housemate.web.client.place.HousematePlaceHistoryMapper;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

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
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ApplicationData, GWTProxyApplication>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ApplicationInstanceData, GWTProxyApplicationInstance>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<AutomationData, GWTProxyAutomation>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<CommandData, GWTProxyCommand>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ConditionData, GWTProxyCondition>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<DeviceData, GWTProxyDevice>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<HardwareData, GWTProxyHardware>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ListData<HousemateData<?>>, GWTProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<OptionData, GWTProxyOption>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ParameterData, GWTProxyParameter>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<PropertyData, GWTProxyProperty>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<RealClientData, GWTProxyRealClient>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<SubTypeData, GWTProxySubType>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<TaskData, GWTProxyTask>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<TypeData<HousemateData<?>>, GWTProxyType>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<UserData, GWTProxyUser>>() {}));
        install(new GinFactoryModuleBuilder().build(new TypeLiteral<HousemateObjectFactory<ValueData, GWTProxyValue>>() {}));
        bind(new TypeLiteral<HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).to(GWTProxyFactory.class);
        // other bits
        bind(LoginManager.class).in(Singleton.class);
        bind(GWTProxyRoot.class).in(Singleton.class);
        bind(PropertyRepository.class).to(GWTPropertyRepository.class).in(Singleton.class);
        bind(Log.class).to(GWTLog.class).in(Singleton.class);
        bind(GWTRouter.class).in(Singleton.class);
        bind(Router.class).to(GWTRouter.class);
        bind(new Key<ProxyFeatureFactory<GWTProxyFeature, GWTProxyDevice>>() {}).to(GWTProxyFeatureFactory.class);
        bind(RegexMatcherFactory.class).to(GWTRegexMatcherFactory.class);
        bind(ListenersFactory.class).to(GWTListenersFactory.class);
    }
}

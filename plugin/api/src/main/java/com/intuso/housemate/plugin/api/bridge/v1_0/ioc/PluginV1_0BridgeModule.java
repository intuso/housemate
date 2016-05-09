package com.intuso.housemate.plugin.api.bridge.v1_0.ioc;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intuso.housemate.client.real.api.bridge.v1_0.*;
import com.intuso.housemate.client.v1_0.real.api.RealType;
import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.client.v1_0.real.api.driver.ConditionDriver;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import com.intuso.housemate.client.v1_0.real.api.driver.HardwareDriver;
import com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver;
import com.intuso.housemate.plugin.api.bridge.v1_0.PluginResourceMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.TypeInfoBridge;
import com.intuso.housemate.plugin.v1_0.api.PluginResource;

/**
 * Created by tomc on 26/10/15.
 */
public class PluginV1_0BridgeModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public com.intuso.housemate.client.real.api.internal.annotations.TypeInfo getTypeInfo(TypeInfo typeInfo) {
        return new TypeInfoBridge(typeInfo);
    }

    @Provides
    @Singleton
    public Iterable<? extends com.intuso.housemate.client.real.api.internal.RealType<?, ?>> getTypes(TypeMapper typeMapper, Iterable<? extends RealType<?, ?>> types) {
        return Lists.newArrayList(Iterables.transform(types, typeMapper.getFromV1_0Function()));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.plugin.api.internal.PluginResource<? extends com.intuso.housemate.client.real.api.internal.driver.ConditionDriver.Factory<?>>> getConditionDrivers(PluginResourceMapper pluginResourceMapper, ConditionDriverFactoryMapper conditionDriverFactoryMapper, Iterable<PluginResource<? extends ConditionDriver.Factory<?>>> conditionDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)conditionDriverFactories, pluginResourceMapper.getFromV1_0Function(conditionDriverFactoryMapper.getFromV1_0Function())));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.plugin.api.internal.PluginResource<? extends com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<?>>> getDeviceDrivers(PluginResourceMapper pluginResourceMapper, DeviceDriverFactoryMapper deviceDriverFactoryMapper, Iterable<PluginResource<? extends DeviceDriver.Factory<?>>> deviceDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)deviceDriverFactories, pluginResourceMapper.getFromV1_0Function(deviceDriverFactoryMapper.getFromV1_0Function())));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.plugin.api.internal.PluginResource<? extends com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<?>>> getHardwareDrivers(PluginResourceMapper pluginResourceMapper, HardwareDriverFactoryMapper hardwareDriverFactoryMapper, Iterable<PluginResource<? extends HardwareDriver.Factory<?>>> hardwareDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)hardwareDriverFactories, pluginResourceMapper.getFromV1_0Function(hardwareDriverFactoryMapper.getFromV1_0Function())));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.plugin.api.internal.PluginResource<? extends com.intuso.housemate.client.real.api.internal.driver.TaskDriver.Factory<?>>> getTaskDrivers(PluginResourceMapper pluginResourceMapper, TaskDriverFactoryMapper taskDriverFactoryMapper, Iterable<PluginResource<? extends TaskDriver.Factory<?>>> taskDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)taskDriverFactories, pluginResourceMapper.getFromV1_0Function(taskDriverFactoryMapper.getFromV1_0Function())));
    }
}

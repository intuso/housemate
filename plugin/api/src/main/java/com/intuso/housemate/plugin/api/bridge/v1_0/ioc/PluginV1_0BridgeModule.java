package com.intuso.housemate.plugin.api.bridge.v1_0.ioc;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intuso.housemate.plugin.api.bridge.v1_0.PluginResourceMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.TypeInfoBridge;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.ConditionDriverFactoryMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.DeviceDriverFactoryMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.HardwareDriverFactoryMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.TaskDriverFactoryMapper;
import com.intuso.housemate.plugin.v1_0.api.annotations.TypeInfo;
import com.intuso.housemate.plugin.v1_0.api.driver.ConditionDriver;
import com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver;
import com.intuso.housemate.plugin.v1_0.api.driver.HardwareDriver;
import com.intuso.housemate.plugin.v1_0.api.driver.TaskDriver;
import com.intuso.housemate.plugin.v1_0.api.module.PluginResource;

/**
 * Created by tomc on 26/10/15.
 */
public class PluginV1_0BridgeModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public com.intuso.housemate.plugin.api.internal.annotations.TypeInfo getTypeInfo(TypeInfo typeInfo) {
        return new TypeInfoBridge(typeInfo);
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.plugin.api.internal.module.PluginResource<Class<?>>> getTypes(PluginResourceMapper pluginResourceMapper, Iterable<PluginResource<Class<?>>> types) {
        return Lists.newArrayList(Iterables.transform((Iterable)types, pluginResourceMapper.getFromV1_0Function(new Function<Class<?>, Class<?>>() {
            @Override
            public Class<?> apply(Class<?> typeClass) {
                return typeClass;
            }
        })));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.plugin.api.internal.module.PluginResource<? extends com.intuso.housemate.plugin.api.internal.driver.ConditionDriver.Factory<?>>> getConditionDrivers(PluginResourceMapper pluginResourceMapper, ConditionDriverFactoryMapper conditionDriverFactoryMapper, Iterable<PluginResource<? extends ConditionDriver.Factory<?>>> conditionDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)conditionDriverFactories, pluginResourceMapper.getFromV1_0Function(conditionDriverFactoryMapper.getFromV1_0Function())));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.plugin.api.internal.module.PluginResource<? extends com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>>> getDeviceDrivers(PluginResourceMapper pluginResourceMapper, DeviceDriverFactoryMapper deviceDriverFactoryMapper, Iterable<PluginResource<? extends DeviceDriver.Factory<?>>> deviceDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)deviceDriverFactories, pluginResourceMapper.getFromV1_0Function(deviceDriverFactoryMapper.getFromV1_0Function())));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.plugin.api.internal.module.PluginResource<? extends com.intuso.housemate.plugin.api.internal.driver.HardwareDriver.Factory<?>>> getHardwareDrivers(PluginResourceMapper pluginResourceMapper, HardwareDriverFactoryMapper hardwareDriverFactoryMapper, Iterable<PluginResource<? extends HardwareDriver.Factory<?>>> hardwareDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)hardwareDriverFactories, pluginResourceMapper.getFromV1_0Function(hardwareDriverFactoryMapper.getFromV1_0Function())));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.plugin.api.internal.module.PluginResource<? extends com.intuso.housemate.plugin.api.internal.driver.TaskDriver.Factory<?>>> getTaskDrivers(PluginResourceMapper pluginResourceMapper, TaskDriverFactoryMapper taskDriverFactoryMapper, Iterable<PluginResource<? extends TaskDriver.Factory<?>>> taskDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)taskDriverFactories, pluginResourceMapper.getFromV1_0Function(taskDriverFactoryMapper.getFromV1_0Function())));
    }
}

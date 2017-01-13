package com.intuso.housemate.client.real.api.bridge.v1_0.ioc;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intuso.housemate.client.api.bridge.v1_0.annotation.IdBridge;
import com.intuso.housemate.client.api.bridge.v1_0.driver.ConditionDriverFactoryMapper;
import com.intuso.housemate.client.api.bridge.v1_0.driver.FeatureDriverFactoryMapper;
import com.intuso.housemate.client.api.bridge.v1_0.driver.HardwareDriverFactoryMapper;
import com.intuso.housemate.client.api.bridge.v1_0.driver.TaskDriverFactoryMapper;
import com.intuso.housemate.client.api.bridge.v1_0.plugin.*;
import com.intuso.housemate.client.real.api.bridge.v1_0.PluginResourceMapper;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.driver.ConditionDriver;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import com.intuso.housemate.client.v1_0.api.driver.TaskDriver;
import com.intuso.housemate.client.v1_0.api.plugin.*;

/**
 * Created by tomc on 26/10/15.
 */
public class PluginV1_0BridgeModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public com.intuso.housemate.client.api.internal.annotation.Id getTypeInfo(Id id) {
        return new IdBridge(id);
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.client.api.internal.plugin.ChoiceType> getChoiceTypes(Iterable<ChoiceType> regexTypes) {
        return Lists.newArrayList(Iterables.transform(regexTypes, new Function<ChoiceType, com.intuso.housemate.client.api.internal.plugin.ChoiceType>() {
            @Override
            public com.intuso.housemate.client.api.internal.plugin.ChoiceType apply(ChoiceType choiceType) {
                return new ChoiceTypeBridge(choiceType);
            }
        }));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.client.api.internal.plugin.CompositeType> getCompositeTypes(Iterable<CompositeType> regexTypes) {
        return Lists.newArrayList(Iterables.transform(regexTypes, new Function<CompositeType, com.intuso.housemate.client.api.internal.plugin.CompositeType>() {
            @Override
            public com.intuso.housemate.client.api.internal.plugin.CompositeType apply(CompositeType compositeType) {
                return new CompositeTypeBridge(compositeType);
            }
        }));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.client.api.internal.plugin.DoubleRangeType> getDoubleRangeTypes(Iterable<DoubleRangeType> DoubleRangeTypes) {
        return Lists.newArrayList(Iterables.transform(DoubleRangeTypes, new Function<DoubleRangeType, com.intuso.housemate.client.api.internal.plugin.DoubleRangeType>() {
            @Override
            public com.intuso.housemate.client.api.internal.plugin.DoubleRangeType apply(DoubleRangeType doubleRangeType) {
                return new DoubleRangeTypeBridge(doubleRangeType);
            }
        }));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.client.api.internal.plugin.IntegerRangeType> getIntegerRangeTypes(Iterable<IntegerRangeType> IntegerRangeTypes) {
        return Lists.newArrayList(Iterables.transform(IntegerRangeTypes, new Function<IntegerRangeType, com.intuso.housemate.client.api.internal.plugin.IntegerRangeType>() {
            @Override
            public com.intuso.housemate.client.api.internal.plugin.IntegerRangeType apply(IntegerRangeType integerRangeType) {
                return new IntegerRangeTypeBridge(integerRangeType);
            }
        }));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.client.api.internal.plugin.RegexType> getRegexTypes(Iterable<RegexType> regexTypes) {
        return Lists.newArrayList(Iterables.transform(regexTypes, new Function<RegexType, com.intuso.housemate.client.api.internal.plugin.RegexType>() {
            @Override
            public com.intuso.housemate.client.api.internal.plugin.RegexType apply(RegexType regexType) {
                return new RegexTypeBridge(regexType);
            }
        }));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.client.api.internal.plugin.PluginResource<? extends com.intuso.housemate.client.api.internal.driver.ConditionDriver.Factory<?>>> getConditionDrivers(PluginResourceMapper pluginResourceMapper, ConditionDriverFactoryMapper conditionDriverFactoryMapper, Iterable<PluginResource<? extends ConditionDriver.Factory<?>>> conditionDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)conditionDriverFactories, pluginResourceMapper.getFromV1_0Function(conditionDriverFactoryMapper.getFromV1_0Function())));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.client.api.internal.plugin.PluginResource<? extends com.intuso.housemate.client.api.internal.driver.FeatureDriver.Factory<?>>> getDeviceDrivers(PluginResourceMapper pluginResourceMapper, FeatureDriverFactoryMapper featureDriverFactoryMapper, Iterable<PluginResource<? extends FeatureDriver.Factory<?>>> deviceDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)deviceDriverFactories, pluginResourceMapper.getFromV1_0Function(featureDriverFactoryMapper.getFromV1_0Function())));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.client.api.internal.plugin.PluginResource<? extends com.intuso.housemate.client.api.internal.driver.HardwareDriver.Factory<?>>> getHardwareDrivers(PluginResourceMapper pluginResourceMapper, HardwareDriverFactoryMapper hardwareDriverFactoryMapper, Iterable<PluginResource<? extends HardwareDriver.Factory<?>>> hardwareDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)hardwareDriverFactories, pluginResourceMapper.getFromV1_0Function(hardwareDriverFactoryMapper.getFromV1_0Function())));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.client.api.internal.plugin.PluginResource<? extends com.intuso.housemate.client.api.internal.driver.TaskDriver.Factory<?>>> getTaskDrivers(PluginResourceMapper pluginResourceMapper, TaskDriverFactoryMapper taskDriverFactoryMapper, Iterable<PluginResource<? extends TaskDriver.Factory<?>>> taskDriverFactories) {
        return Lists.newArrayList(Iterables.transform((Iterable)taskDriverFactories, pluginResourceMapper.getFromV1_0Function(taskDriverFactoryMapper.getFromV1_0Function())));
    }
}

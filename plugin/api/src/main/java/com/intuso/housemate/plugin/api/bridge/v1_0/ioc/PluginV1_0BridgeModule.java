package com.intuso.housemate.plugin.api.bridge.v1_0.ioc;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intuso.housemate.client.real.api.bridge.v1_0.TypeMapper;
import com.intuso.housemate.client.v1_0.real.api.RealType;
import com.intuso.housemate.plugin.api.bridge.v1_0.*;
import com.intuso.housemate.plugin.v1_0.api.*;

/**
 * Created by tomc on 26/10/15.
 */
public class PluginV1_0BridgeModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public com.intuso.housemate.plugin.api.internal.TypeInfo getTypeInfo(TypeInfo typeInfo) {
        return new TypeInfoBridge(typeInfo);
    }

    @Provides
    @Singleton
    public Iterable<? extends com.intuso.housemate.client.real.api.internal.RealType<?>> getTypes(TypeMapper typeMapper, Iterable<? extends RealType<?>> types) {
        return Lists.newArrayList(Iterables.transform(types, typeMapper.getFromV1_0Function()));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.plugin.api.internal.PluginResource<? extends com.intuso.housemate.plugin.api.internal.Comparator<?>>> getComparators(PluginResourceMapper pluginResourceMapper, ComparatorMapper comparatorMapper, Iterable<PluginResource<? extends Comparator<?>>> comparators) {
        return Lists.newArrayList(Iterables.transform((Iterable)comparators, pluginResourceMapper.getFromV1_0Function(comparatorMapper.getFromV1_0Function())));
    }

    @Provides
    @Singleton
    public Iterable<com.intuso.housemate.plugin.api.internal.PluginResource<? extends com.intuso.housemate.plugin.api.internal.Operator<?, ?>>> getOperators(PluginResourceMapper pluginResourceMapper, OperatorMapper operatorMapper, Iterable<PluginResource<? extends Operator<?, ?>>> operators) {
        return Lists.newArrayList(Iterables.transform((Iterable)operators, pluginResourceMapper.getFromV1_0Function(operatorMapper.getFromV1_0Function())));
    }

    @Provides
    @Singleton
    public Iterable<? extends com.intuso.housemate.plugin.api.internal.Transformer<?, ?>> getTransformers(TransformerMapper transformerMapper, Iterable<? extends Transformer<?, ?>> transformers) {
        return Lists.newArrayList(Iterables.transform(transformers, transformerMapper.getFromV1_0Function()));
    }
}

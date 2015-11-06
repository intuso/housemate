package com.intuso.housemate.plugin.api.bridge.v1_0.ioc;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intuso.housemate.client.real.api.bridge.v1_0.TypeMapper;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.plugin.api.bridge.v1_0.ComparatorMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.OperatorMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.TransformerMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.TypeInfoBridge;
import com.intuso.housemate.plugin.api.internal.Comparator;
import com.intuso.housemate.plugin.api.internal.Operator;
import com.intuso.housemate.plugin.api.internal.Transformer;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;

import java.util.Set;

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
    public Set<RealType<?>> getTypes(TypeMapper typeMapper, Set<com.intuso.housemate.client.v1_0.real.api.RealType<?>> types) {
        return Sets.newHashSet(Iterables.transform(types, typeMapper.getFromV1_0Function()));
    }

    @Provides
    @Singleton
    public Set<Comparator<?>> getComparators(ComparatorMapper operatorMapper, Set<com.intuso.housemate.plugin.v1_0.api.Comparator<?>> operators) {
        return Sets.newHashSet(Iterables.transform(operators, operatorMapper.getFromV1_0Function()));
    }

    @Provides
    @Singleton
    public Set<Operator<?, ?>> getOperators(OperatorMapper operatorMapper, Set<com.intuso.housemate.plugin.v1_0.api.Operator<?, ?>> operators) {
        return Sets.newHashSet(Iterables.transform(operators, operatorMapper.getFromV1_0Function()));
    }

    @Provides
    @Singleton
    public Set<Transformer<?, ?>> getTransformers(TransformerMapper operatorMapper, Set<com.intuso.housemate.plugin.v1_0.api.Transformer<?, ?>> operators) {
        return Sets.newHashSet(Iterables.transform(operators, operatorMapper.getFromV1_0Function()));
    }
}

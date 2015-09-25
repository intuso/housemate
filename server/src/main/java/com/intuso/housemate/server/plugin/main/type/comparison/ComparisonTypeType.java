package com.intuso.housemate.server.plugin.main.type.comparison;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.real.api.internal.RealOption;
import com.intuso.housemate.client.real.api.internal.impl.type.RealChoiceType;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.housemate.plugin.api.internal.Comparator;
import com.intuso.housemate.plugin.api.internal.ComparisonType;
import com.intuso.housemate.plugin.api.internal.PluginListener;
import com.intuso.housemate.plugin.host.PluginManager;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.Set;

/**
 */
public class ComparisonTypeType extends RealChoiceType<ComparisonType> implements PluginListener {

    public final static String ID = "comparison-type";
    public final static String NAME = "Comparison Type";
    public final static String DESCRIPTION = "Type for comparing values";

    private final ListenersFactory listenersFactory;

    private final TypeSerialiser<ComparisonType> serialiser;

    @Inject
    public ComparisonTypeType(Log log, ListenersFactory listenersFactory, TypeSerialiser<ComparisonType> serialiser, PluginManager pluginManager) {
        super(log, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.listenersFactory = listenersFactory;
        this.serialiser = serialiser;
        pluginManager.addPluginListener(this, true);
    }

    @Override
    public TypeInstance serialise(ComparisonType type) {
        return serialiser.serialise(type);
    }

    @Override
    public ComparisonType deserialise(TypeInstance instance) {
        return serialiser.deserialise(instance);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        for(Comparator<?> comparator : pluginInjector.getInstance(new Key<Set<Comparator<?>>>() {}))
            if(getOptions().getChild(comparator.getComparisonType().getId()) == null)
                getOptions().add(new RealOption(getLog(), listenersFactory,
                        comparator.getComparisonType().getId(), comparator.getComparisonType().getName(), comparator.getComparisonType().getDescription()));
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        // todo remove them, not so easy as there might be many values for one key
    }

    public final static class Serialiser implements TypeSerialiser<ComparisonType>, PluginListener {

        private final Map<String, ComparisonType> types = Maps.newHashMap();

        @Inject
        public Serialiser(PluginManager pluginManager) {
            pluginManager.addPluginListener(this, true);
        }

        @Override
        public TypeInstance serialise(ComparisonType type) {
            return type != null ? new TypeInstance(type.getId()) : null;
        }

        @Override
        public ComparisonType deserialise(TypeInstance instance) {
            return instance != null ? types.get(instance.getValue()) : null;
        }

        @Override
        public void pluginAdded(Injector pluginInjector) {
            for(Comparator<?> comparator : pluginInjector.getInstance(new Key<Set<Comparator<?>>>() {}))
                if(types.get(comparator.getComparisonType().getId()) == null)
                    types.put(comparator.getComparisonType().getId(), comparator.getComparisonType());
        }

        @Override
        public void pluginRemoved(Injector pluginInjector) {
            // todo remove them, not so easy as there might be many values for one key
        }
    }
}

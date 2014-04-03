package com.intuso.housemate.server.plugin.main.type.comparison;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.ComparisonType;
import com.intuso.housemate.server.plugin.PluginListener;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.Set;

/**
 */
public class ComparisonTypeType extends RealChoiceType<ComparisonType> {

    public final static String ID = "comparison-type";
    public final static String NAME = "Comparison Type";
    public final static String DESCRIPTION = "Type for comparing values";

    private final TypeSerialiser<ComparisonType> serialiser;

    @Inject
    public ComparisonTypeType(Log log, TypeSerialiser<ComparisonType> serialiser) {
        super(log, ID, NAME, DESCRIPTION, 1, 1);
        this.serialiser = serialiser;
    }

    @Override
     public TypeInstance serialise(ComparisonType type) {
        return serialiser.serialise(type);
    }

    @Override
    public com.intuso.housemate.plugin.api.ComparisonType deserialise(TypeInstance instance) {
        return serialiser.deserialise(instance);
    }

    public final static class Serialiser implements TypeSerialiser<ComparisonType>, PluginListener {

        private final Log log;
        private final Map<String, ComparisonType> types = Maps.newHashMap();

        @Inject
        public Serialiser(Log log, PluginManager pluginManager) {
            this.log = log;
            pluginManager.addPluginListener(this, true);
        }

        @Override
        public TypeInstance serialise(ComparisonType type) {
            return type != null ? new TypeInstance(type.getId()) : null;
        }

        @Override
        public com.intuso.housemate.plugin.api.ComparisonType deserialise(TypeInstance instance) {
            return instance != null ? types.get(instance.getValue()) : null;
        }

        @Override
        public void pluginAdded(Injector pluginInjector) {
            ComparisonTypeType type = (ComparisonTypeType) types.get(ID);
            if(type != null) {
                for(Comparator<?> comparator : pluginInjector.getInstance(new Key<Set<Comparator<?>>>() {})) {
                    if(types.get(comparator.getComparisonType().getId()) == null) {
                        types.put(comparator.getComparisonType().getId(), comparator.getComparisonType());
                        type.getOptions().add(new RealOption(log, comparator.getComparisonType().getId(),
                                comparator.getComparisonType().getName(), comparator.getComparisonType().getDescription()));
                    }
                }
            }
        }

        @Override
        public void pluginRemoved(Injector pluginInjector) {
            // todo remove them
        }
    }
}
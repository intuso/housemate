package com.intuso.housemate.server.plugin.main.type.comparison;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.RealCompoundType;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.server.plugin.PluginListener;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSourceType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.Set;

/**
 */
public class ComparisonType extends RealCompoundType<Comparison> implements PluginListener {

    public final static String ID = "comparison";
    public final static String NAME = "Comparison";
    public final static String DESCRIPTION = "Comparison of values";
    
    public final static String COMPARISON_TYPE_ID = "comparison-type";
    public final static String COMPARISON_TYPE_NAME = "Comparison Type";
    public final static String COMPARISON_TYPE_DESCRIPTION = "The way to compare the values";
    public final static String VALUE_0_ID = "value0";
    public final static String VALUE_0_NAME = "First value";
    public final static String VALUE_0_DESCRIPTION = "The first value to compare";
    public final static String VALUE_1_ID = "value1";
    public final static String VALUE_1_NAME = "Second value";
    public final static String VALUE_1_DESCRIPTION = "The second value to compare";

    private final Log log;
    private final TypeSerialiser<com.intuso.housemate.plugin.api.ComparisonType> comparisonTypeSerialiser;
    private final TypeSerialiser<ValueSource> sourceTypeSerialiser;

    private final Map<com.intuso.housemate.plugin.api.ComparisonType, Map<String, Comparator<?>>> comparators = Maps.newHashMap();

    @Inject
    public ComparisonType(Log log,
                          ListenersFactory listenersFactory,
                          RealList<TypeData<?>, RealType<?, ?, ?>> types,
                          TypeSerialiser<com.intuso.housemate.plugin.api.ComparisonType> comparisonTypeSerialiser,
                          TypeSerialiser<ValueSource> sourceTypeSerialiser,
                          PluginManager pluginManager) {
        super(log, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.log = log;
        this.comparisonTypeSerialiser = comparisonTypeSerialiser;
        this.sourceTypeSerialiser = sourceTypeSerialiser;
        getSubTypes().add(new RealSubType<com.intuso.housemate.plugin.api.ComparisonType>(log, listenersFactory, COMPARISON_TYPE_ID,
                COMPARISON_TYPE_NAME, COMPARISON_TYPE_DESCRIPTION, ComparisonTypeType.ID, types));
        getSubTypes().add(new RealSubType<ValueSource>(log, listenersFactory, VALUE_0_ID, VALUE_0_NAME,
                VALUE_0_DESCRIPTION, ValueSourceType.ID, types));
        getSubTypes().add(new RealSubType<ValueSource>(log, listenersFactory, VALUE_1_ID, VALUE_1_NAME,
                VALUE_1_DESCRIPTION, ValueSourceType.ID, types));
        pluginManager.addPluginListener(this, true);
    }

    @Override
    public TypeInstance serialise(Comparison comparisonInstance) {
        if(comparisonInstance == null)
            return null;
        TypeInstance result = new TypeInstance();
        result.getChildValues().getChildren().put(COMPARISON_TYPE_ID, new TypeInstances(comparisonTypeSerialiser.serialise(comparisonInstance.getComparisonType())));
        result.getChildValues().getChildren().put(VALUE_0_ID, new TypeInstances(sourceTypeSerialiser.serialise(comparisonInstance.getFirstValueSource())));
        result.getChildValues().getChildren().put(VALUE_1_ID, new TypeInstances(sourceTypeSerialiser.serialise(comparisonInstance.getSecondValueSource())));
        return result;
    }

    @Override
    public Comparison deserialise(TypeInstance instance) {
        if(instance == null
                || instance.getChildValues().getChildren().get(COMPARISON_TYPE_ID) == null
                || instance.getChildValues().getChildren().get(COMPARISON_TYPE_ID).getElements().size() == 0)
            return null;
        ValueSource value0 = null;
        if(instance.getChildValues().getChildren().get(VALUE_0_ID) != null && instance.getChildValues().getChildren().get(VALUE_0_ID).getElements().size() != 0)
            value0 = sourceTypeSerialiser.deserialise(instance.getChildValues().getChildren().get(VALUE_0_ID).getElements().get(0));
        ValueSource value1 = null;
        if(instance.getChildValues().getChildren().get(VALUE_1_ID) != null && instance.getChildValues().getChildren().get(VALUE_1_ID).getElements().size() != 0)
            value1 = sourceTypeSerialiser.deserialise(instance.getChildValues().getChildren().get(VALUE_1_ID).getElements().get(0));
        com.intuso.housemate.plugin.api.ComparisonType comparisonType = comparisonTypeSerialiser.deserialise(instance.getChildValues().getChildren().get(COMPARISON_TYPE_ID).getElements().get(0));
        return new Comparison(comparisonType, comparators.get(comparisonType), value0, value1);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        for(Comparator<?> comparator : pluginInjector.getInstance(new Key<Set<Comparator<?>>>() {})) {
            Map<String, Comparator<?>> comparatorsByType = comparators.get(comparator.getComparisonType());
            if(comparatorsByType == null) {
                comparatorsByType = Maps.newHashMap();
                comparators.put(comparator.getComparisonType(), comparatorsByType);
            }
            comparatorsByType.put(comparator.getTypeId(), comparator);
        }
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        // todo remove them
    }
}

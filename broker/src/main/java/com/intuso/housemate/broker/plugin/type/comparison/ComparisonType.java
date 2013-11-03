package com.intuso.housemate.broker.plugin.type.comparison;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.type.valuesource.ValueSource;
import com.intuso.housemate.broker.plugin.type.valuesource.ValueSourceType;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.impl.type.RealCompoundType;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.PluginDescriptor;

import java.util.Map;

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

    private final ComparisonTypeType comparisonTypeType;
    private final ValueSourceType sourceType;
    private final Map<com.intuso.housemate.plugin.api.ComparisonType, Map<String, Comparator<?>>> comparators = Maps.newHashMap();

    private final BrokerGeneralResources generalResources;

    public ComparisonType(RealResources resources, BrokerGeneralResources generalResources) {
        this(resources,  generalResources,
                (ComparisonTypeType) generalResources.getClient().getRoot().getTypes().get(ComparisonTypeType.ID),
                (ValueSourceType) generalResources.getClient().getRoot().getTypes().get(ValueSourceType.ID));
    }

    public ComparisonType(RealResources resources, BrokerGeneralResources generalResources,
                          ComparisonTypeType comparisonTypeType, ValueSourceType sourceType) {
        super(resources, ID, NAME, DESCRIPTION, 1, 1);
        this.generalResources = generalResources;
        this.comparisonTypeType = comparisonTypeType;
        this.sourceType = sourceType;
        getSubTypes().add(new RealSubType<com.intuso.housemate.plugin.api.ComparisonType>(resources, COMPARISON_TYPE_ID, COMPARISON_TYPE_NAME,
                COMPARISON_TYPE_DESCRIPTION, comparisonTypeType));
        getSubTypes().add(new RealSubType<ValueSource>(resources, VALUE_0_ID, VALUE_0_NAME, VALUE_0_DESCRIPTION,
                sourceType));
        getSubTypes().add(new RealSubType<ValueSource>(resources, VALUE_1_ID, VALUE_1_NAME, VALUE_1_DESCRIPTION,
                sourceType));
        generalResources.addPluginListener(this, true);
    }

    @Override
    public TypeInstance serialise(Comparison comparisonInstance) {
        if(comparisonInstance == null)
            return null;
        TypeInstance result = new TypeInstance();
        result.getChildValues().put(COMPARISON_TYPE_ID, new TypeInstances(comparisonTypeType.serialise(comparisonInstance.getComparisonType())));
        result.getChildValues().put(VALUE_0_ID, new TypeInstances(sourceType.serialise(comparisonInstance.getFirstValueSource())));
        result.getChildValues().put(VALUE_1_ID, new TypeInstances(sourceType.serialise(comparisonInstance.getSecondValueSource())));
        return result;
    }

    @Override
    public Comparison deserialise(TypeInstance instance) {
        if(instance == null
                || instance.getChildValues().get(COMPARISON_TYPE_ID) == null
                || instance.getChildValues().get(COMPARISON_TYPE_ID).size() == 0)
            return null;
        ValueSource value0 = null;
        if(instance.getChildValues().get(VALUE_0_ID) != null && instance.getChildValues().get(VALUE_0_ID).size() != 0)
            value0 = sourceType.deserialise(instance.getChildValues().get(VALUE_0_ID).get(0));
        ValueSource value1 = null;
        if(instance.getChildValues().get(VALUE_1_ID) != null && instance.getChildValues().get(VALUE_1_ID).size() != 0)
            value1 = sourceType.deserialise(instance.getChildValues().get(VALUE_1_ID).get(0));
        com.intuso.housemate.plugin.api.ComparisonType comparisonType = comparisonTypeType.deserialise(instance.getChildValues().get(COMPARISON_TYPE_ID).get(0));
        return new Comparison(comparisonType, comparators.get(comparisonType), value0, value1);
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(Comparator<?> comparator : plugin.getComparators(generalResources.getClientResources())) {
            Map<String, Comparator<?>> comparatorsByType = comparators.get(comparator.getComparisonType());
            if(comparatorsByType == null) {
                comparatorsByType = Maps.newHashMap();
                comparators.put(comparator.getComparisonType(), comparatorsByType);
            }
            comparatorsByType.put(comparator.getTypeId(), comparator);
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        // todo remove them
    }
}

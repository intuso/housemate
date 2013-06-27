package com.intuso.housemate.broker.plugin.type.comparison;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.type.valuesource.ValueSource;
import com.intuso.housemate.broker.plugin.type.valuesource.ValueSourceType;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.impl.type.RealCompoundType;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.ComparisonOperator;
import com.intuso.housemate.plugin.api.PluginDescriptor;

import java.util.Map;

/**
 */
public class ComparisonType extends RealCompoundType<Comparison> implements PluginListener {

    public final static String ID = "comparison";
    public final static String NAME = "Comparison";
    public final static String DESCRIPTION = "Comparison of values";
    
    public final static String OPERATOR_ID = "operator";
    public final static String OPERATOR_NAME = "Operator";
    public final static String OPERATOR_DESCRIPTION = "The way to compare the values";
    public final static String VALUE_0_ID = "value0";
    public final static String VALUE_0_NAME = "First value";
    public final static String VALUE_0_DESCRIPTION = "The first value to compare";
    public final static String VALUE_1_ID = "value1";
    public final static String VALUE_1_NAME = "Second value";
    public final static String VALUE_1_DESCRIPTION = "The second value to compare";

    private final ComparisonOperatorType operatorType;
    private final ValueSourceType sourceType;
    private final Map<ComparisonOperator, Map<String, Comparator<?>>> comparators = Maps.newHashMap();

    private final BrokerGeneralResources generalResources;

    public ComparisonType(RealResources resources, BrokerGeneralResources generalResources) {
        this(resources,  generalResources,
                (ComparisonOperatorType) generalResources.getClient().getRoot().getTypes().get(ComparisonOperatorType.ID),
                (ValueSourceType) generalResources.getClient().getRoot().getTypes().get(ValueSourceType.ID));
    }

    public ComparisonType(RealResources resources, BrokerGeneralResources generalResources,
                          ComparisonOperatorType operatorType, ValueSourceType sourceType) {
        super(resources, ID, NAME, DESCRIPTION);
        this.generalResources = generalResources;
        this.operatorType = operatorType;
        this.sourceType = sourceType;
        getSubTypes().add(new RealSubType<ComparisonOperator>(resources, OPERATOR_ID, OPERATOR_NAME,
                OPERATOR_DESCRIPTION, operatorType));
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
        result.getChildValues().put(OPERATOR_ID, operatorType.serialise(comparisonInstance.getOperator()));
        result.getChildValues().put(VALUE_0_ID, sourceType.serialise(comparisonInstance.getFirstValueSource()));
        result.getChildValues().put(VALUE_1_ID, sourceType.serialise(comparisonInstance.getSecondValueSource()));
        return result;
    }

    @Override
    public Comparison deserialise(TypeInstance instance) {
        if(instance == null)
            return null;
        ComparisonOperator operator = operatorType.deserialise(instance.getChildValues().get(OPERATOR_ID));
        return new Comparison(operator, comparators.get(operator),
                sourceType.deserialise(instance.getChildValues().get(VALUE_0_ID)),
                sourceType.deserialise(instance.getChildValues().get(VALUE_1_ID)));
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(Comparator<?> comparator : plugin.getComparators(generalResources.getClientResources())) {
            Map<String, Comparator<?>> comparatorsByType = comparators.get(comparator.getOperator());
            if(comparatorsByType == null) {
                comparatorsByType = Maps.newHashMap();
                comparators.put(comparator.getOperator(), comparatorsByType);
            }
            comparatorsByType.put(comparator.getTypeId(), comparator);
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        // todo remove them
    }
}

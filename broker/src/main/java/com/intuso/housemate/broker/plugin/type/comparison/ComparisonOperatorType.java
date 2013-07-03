package com.intuso.housemate.broker.plugin.type.comparison;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.ComparisonOperator;
import com.intuso.housemate.plugin.api.PluginDescriptor;

import java.util.Map;

/**
 */
public class ComparisonOperatorType extends RealChoiceType<ComparisonOperator> implements PluginListener {

    public final static String ID = "comparison-operator";
    public final static String NAME = "Comparison Operator";
    public final static String DESCRIPTION = "Operator for comparing values";

    private final BrokerGeneralResources generalResources;

    private final Map<String, ComparisonOperator> operators = Maps.newHashMap();

    public ComparisonOperatorType(RealResources resources, BrokerGeneralResources generalResources) {
        super(resources, ID, NAME, DESCRIPTION, 1, 1);
        this.generalResources = generalResources;
        generalResources.addPluginListener(this, true);
    }

    @Override
     public TypeInstance serialise(ComparisonOperator operator) {
        return operator != null ? new TypeInstance(operator.getId()) : null;
    }

    @Override
    public ComparisonOperator deserialise(TypeInstance instance) {
        return instance != null ? operators.get(instance.getValue()) : null;
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(Comparator<?> comparator : plugin.getComparators(generalResources.getClientResources())) {
            if(operators.get(comparator.getOperator().getId()) == null) {
                operators.put(comparator.getOperator().getId(), comparator.getOperator());
                getOptions().add(new RealOption(getResources(), comparator.getOperator().getId(),
                        comparator.getOperator().getName(), comparator.getOperator().getDescription()));
            }
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        // todo remove them
    }
}

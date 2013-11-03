package com.intuso.housemate.broker.plugin.type.comparison;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.plugin.api.*;
import com.intuso.housemate.plugin.api.ComparisonType;

import java.util.Map;

/**
 */
public class ComparisonTypeType extends RealChoiceType<ComparisonType> implements PluginListener {

    public final static String ID = "comparison-type";
    public final static String NAME = "Comparison Type";
    public final static String DESCRIPTION = "Type for comparing values";

    private final BrokerGeneralResources generalResources;

    private final Map<String, ComparisonType> types = Maps.newHashMap();

    public ComparisonTypeType(RealResources resources, BrokerGeneralResources generalResources) {
        super(resources, ID, NAME, DESCRIPTION, 1, 1);
        this.generalResources = generalResources;
        generalResources.addPluginListener(this, true);
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
    public void pluginAdded(PluginDescriptor plugin) {
        for(Comparator<?> comparator : plugin.getComparators(generalResources.getClientResources())) {
            if(types.get(comparator.getComparisonType().getId()) == null) {
                types.put(comparator.getComparisonType().getId(), comparator.getComparisonType());
                getOptions().add(new RealOption(getResources(), comparator.getComparisonType().getId(),
                        comparator.getComparisonType().getName(), comparator.getComparisonType().getDescription()));
            }
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        // todo remove them
    }
}

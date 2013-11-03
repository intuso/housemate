package com.intuso.housemate.broker.plugin.type.operation;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.plugin.api.Operator;
import com.intuso.housemate.plugin.api.OperationType;
import com.intuso.housemate.plugin.api.PluginDescriptor;

import java.util.Map;

/**
 */
public class OperationTypeType extends RealChoiceType<OperationType> implements PluginListener {

    public final static String ID = "operation-type";
    public final static String NAME = "Operation Type";
    public final static String DESCRIPTION = "Type of operation";

    private final BrokerGeneralResources generalResources;

    private final Map<String, OperationType> types = Maps.newHashMap();

    public OperationTypeType(RealResources resources, BrokerGeneralResources generalResources) {
        super(resources, ID, NAME, DESCRIPTION, 1, 1);
        this.generalResources = generalResources;
        generalResources.addPluginListener(this, true);
    }

    @Override
     public TypeInstance serialise(OperationType type) {
        return type != null ? new TypeInstance(type.getId()) : null;
    }

    @Override
    public OperationType deserialise(TypeInstance instance) {
        return instance != null ? types.get(instance.getValue()) : null;
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(Operator<?, ?> operator : plugin.getOperators(generalResources.getClientResources())) {
            if(types.get(operator.getOperationType().getId()) == null) {
                types.put(operator.getOperationType().getId(), operator.getOperationType());
                getOptions().add(new RealOption(getResources(), operator.getOperationType().getId(),
                        operator.getOperationType().getName(), operator.getOperationType().getDescription()));
            }
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        // todo remove them
    }
}

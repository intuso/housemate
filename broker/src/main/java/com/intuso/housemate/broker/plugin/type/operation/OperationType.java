package com.intuso.housemate.broker.plugin.type.operation;

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
import com.intuso.housemate.plugin.api.Operator;
import com.intuso.housemate.plugin.api.PluginDescriptor;

import java.util.Map;

/**
 */
public class OperationType extends RealCompoundType<Operation> implements PluginListener {

    public final static String ID = "operation";
    public final static String NAME = "Operation";
    public final static String DESCRIPTION = "Operation of values";
    
    public final static String OPERATION_TYPE_ID = "operation-type";
    public final static String OPERATION_TYPE_NAME = "Operation Type";
    public final static String OPERATION_TYPE_DESCRIPTION = "The type of operation";
    public final static String VALUE_0_ID = "value0";
    public final static String VALUE_0_NAME = "First value";
    public final static String VALUE_0_DESCRIPTION = "The first value";
    public final static String VALUE_1_ID = "value1";
    public final static String VALUE_1_NAME = "Second value";
    public final static String VALUE_1_DESCRIPTION = "The second value";

    private final OperationTypeType operationTypeType;
    private final ValueSourceType sourceType;
    private final Map<com.intuso.housemate.plugin.api.OperationType, Map<String, Operator<?, ?>>> operators = Maps.newHashMap();

    private final BrokerGeneralResources generalResources;

    public OperationType(RealResources resources, BrokerGeneralResources generalResources) {
        this(resources,  generalResources,
                (OperationTypeType) generalResources.getClient().getRoot().getTypes().get(OperationTypeType.ID),
                (ValueSourceType) generalResources.getClient().getRoot().getTypes().get(ValueSourceType.ID));
    }

    public OperationType(RealResources resources, BrokerGeneralResources generalResources,
                         OperationTypeType operationTypeType, ValueSourceType sourceType) {
        super(resources, ID, NAME, DESCRIPTION, 1, 1);
        this.generalResources = generalResources;
        this.operationTypeType = operationTypeType;
        this.sourceType = sourceType;
        getSubTypes().add(new RealSubType<com.intuso.housemate.plugin.api.OperationType>(resources, OPERATION_TYPE_ID, OPERATION_TYPE_NAME,
                OPERATION_TYPE_DESCRIPTION, operationTypeType));
        getSubTypes().add(new RealSubType<ValueSource>(resources, VALUE_0_ID, VALUE_0_NAME, VALUE_0_DESCRIPTION,
                sourceType));
        getSubTypes().add(new RealSubType<ValueSource>(resources, VALUE_1_ID, VALUE_1_NAME, VALUE_1_DESCRIPTION,
                sourceType));
        generalResources.addPluginListener(this, true);
    }

    @Override
    public TypeInstance serialise(Operation operationInstance) {
        if(operationInstance == null)
            return null;
        TypeInstance result = new TypeInstance();
        result.getChildValues().put(OPERATION_TYPE_ID, new TypeInstances(operationTypeType.serialise(operationInstance.getOperationType())));
        result.getChildValues().put(VALUE_0_ID, new TypeInstances(sourceType.serialise(operationInstance.getFirstValueSource())));
        result.getChildValues().put(VALUE_1_ID, new TypeInstances(sourceType.serialise(operationInstance.getSecondValueSource())));
        return result;
    }

    @Override
    public Operation deserialise(TypeInstance instance) {
        if(instance == null
                || instance.getChildValues().get(OPERATION_TYPE_ID) == null
                || instance.getChildValues().get(OPERATION_TYPE_ID).size() == 0)
            return null;
        ValueSource value0 = null;
        if(instance.getChildValues().get(VALUE_0_ID) != null && instance.getChildValues().get(VALUE_0_ID).size() != 0)
            value0 = sourceType.deserialise(instance.getChildValues().get(VALUE_0_ID).get(0));
        ValueSource value1 = null;
        if(instance.getChildValues().get(VALUE_1_ID) != null && instance.getChildValues().get(VALUE_1_ID).size() != 0)
            value1 = sourceType.deserialise(instance.getChildValues().get(VALUE_1_ID).get(0));
        com.intuso.housemate.plugin.api.OperationType operationType = operationTypeType.deserialise(instance.getChildValues().get(OPERATION_TYPE_ID).get(0));
        return new Operation(operationType, operators.get(operationType), value0, value1);
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(Operator<?, ?> operator : plugin.getOperators(generalResources.getClientResources())) {
            Map<String, Operator<?, ?>> operatorsByType = operators.get(operator.getOperationType());
            if(operatorsByType == null) {
                operatorsByType = Maps.newHashMap();
                operators.put(operator.getOperationType(), operatorsByType);
            }
        operatorsByType.put(operator.getInputTypeId(), operator);
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        // todo remove them
    }
}

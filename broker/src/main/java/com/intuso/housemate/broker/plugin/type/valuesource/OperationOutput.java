package com.intuso.housemate.broker.plugin.type.valuesource;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.type.operation.Operation;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.Operator;

/**
 */
public class OperationOutput extends ValueSource implements ValueAvailableListener {

    private final BrokerGeneralResources generalResources;
    private final Operation operation;
    private ComputedValue value;

    public OperationOutput(BrokerGeneralResources generalResources, Operation operation) {
        this.generalResources = generalResources;
        this.operation = operation;
        operation.getFirstValueSource().addValueAvailableListener(this);
        operation.getSecondValueSource().addValueAvailableListener(this);
    }

    public Operation getOperation() {
        return operation;
    }

    private void operate() {

        Value firstValue = operation.getFirstValueSource().getValue();
        Value secondValue = operation.getFirstValueSource().getValue();

        if(firstValue == null || secondValue == null) {
            if(value != null) {
                value = null;
                for(ValueAvailableListener listener : listeners)
                    listener.valueUnavailable(this);
            }
            return;
        }

        if(firstValue.getType().getId().equals(secondValue.getType().getId())) {
            RealType<?, ?, Object> inputType = (RealType<?, ?, Object>) generalResources.getClient().getRoot().getTypes().get(firstValue.getType().getId());
            Operator<Object, Object> operator = (Operator<Object, Object>) operation.getOperatorsByType().get(inputType.getId());
            RealType<?, ?, Object> outputType = (RealType<?, ?, Object>) generalResources.getClient().getRoot().getTypes().get(operator.getOutputTypeId());
            try {
                Object result = operator.apply(inputType.deserialise(firstValue.getTypeInstances().get(0)), inputType.deserialise(secondValue.getTypeInstances().get(0)));
                if(value != null && !value.getType().getId().equals(outputType.getId())) {
                    for(ValueAvailableListener listener : listeners)
                        listener.valueUnavailable(this);
                    value = null;
                }
                if(value == null)
                    value = new ComputedValue(outputType);
                value.setTypeInstances(new TypeInstances(outputType.serialise(result)));
            } catch(HousemateException e) {
                generalResources.getLog().e("Failed to operate on values");
                generalResources.getLog().st(e);
                value = null;
                return;
            }
        } else {
            value = null;
            for(ValueAvailableListener listener : listeners)
                listener.valueUnavailable(this);
        }
    }

    @Override
    public Value<?, ?> getValue() {
        return value;
    }

    @Override
    public void valueAvailable(ValueSource source, Value<?, ?> value) {
        operate();
    }

    @Override
    public void valueUnavailable(ValueSource source) {
        if(value != null) {
            value = null;
            for(ValueAvailableListener listener : listeners)
                listener.valueUnavailable(this);
        }
    }
}

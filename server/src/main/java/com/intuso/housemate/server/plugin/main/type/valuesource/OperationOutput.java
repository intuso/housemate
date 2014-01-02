package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.server.plugin.main.type.operation.Operation;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.Operator;
import com.intuso.utilities.log.Log;

/**
 */
public class OperationOutput extends ValueSource implements ValueAvailableListener {

    private final Log log;
    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final Operation operation;
    private ComputedValue value;

    public OperationOutput(Log log, RealList<TypeData<?>, RealType<?, ?, ?>> types, Operation operation) {
        this.log = log;
        this.types = types;
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
            RealType<?, ?, Object> inputType = (RealType<?, ?, Object>) types.get(firstValue.getType().getId());
            Operator<Object, Object> operator = (Operator<Object, Object>) operation.getOperatorsByType().get(inputType.getId());
            RealType<?, ?, Object> outputType = (RealType<?, ?, Object>) types.get(operator.getOutputTypeId());
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
                log.e("Failed to operate on values");
                log.st(e);
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

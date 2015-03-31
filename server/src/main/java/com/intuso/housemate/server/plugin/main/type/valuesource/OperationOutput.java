package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.Operator;
import com.intuso.housemate.server.plugin.main.type.operation.Operation;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class OperationOutput extends ValueSource implements ValueAvailableListener {

    private final Log log;
    private final ListenersFactory listenersFactory;
    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final Operation operation;
    private ComputedValue value;

    public OperationOutput(Log log, ListenersFactory listenersFactory, RealList<TypeData<?>, RealType<?, ?, ?>> types, Operation operation) {
        super(listenersFactory);
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.types = types;
        this.operation = operation;
        operation.getFirstValueSource().addValueAvailableListener(this, true);
        operation.getSecondValueSource().addValueAvailableListener(this, true);
    }

    public Operation getOperation() {
        return operation;
    }

    private void operate() {

        Value firstValue = operation.getFirstValueSource().getValue();
        Value secondValue = operation.getSecondValueSource().getValue();

        if(firstValue == null || secondValue == null) {
            if(value != null) {
                value = null;
                for(ValueAvailableListener listener : listeners)
                    listener.valueUnavailable(this);
            }
            return;
        }

        if(firstValue.getTypeId().equals(secondValue.getTypeId())) {
            RealType<?, ?, Object> inputType = (RealType<?, ?, Object>) types.get(firstValue.getTypeId());
            Operator<Object, Object> operator = (Operator<Object, Object>) operation.getOperatorsByType().get(inputType.getId());
            RealType<?, ?, Object> outputType = (RealType<?, ?, Object>) types.get(operator.getOutputTypeId());
            try {
                Object result = operator.apply(inputType.deserialise(firstValue.getTypeInstances().getElements().get(0)), inputType.deserialise(secondValue.getTypeInstances().getElements().get(0)));
                if(value != null && !value.getTypeId().equals(outputType.getId())) {
                    for(ValueAvailableListener listener : listeners)
                        listener.valueUnavailable(this);
                    value = null;
                }
                if(value == null)
                    value = new ComputedValue(listenersFactory, outputType);
                value.setTypeInstances(new TypeInstances(outputType.serialise(result)));
            } catch(HousemateException e) {
                log.e("Failed to operate on values", e);
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

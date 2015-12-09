package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.housemate.plugin.api.internal.Operator;
import com.intuso.housemate.server.plugin.main.type.operation.Operation;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class OperationOutput extends ValueSource implements ValueAvailableListener {

    private final Logger logger;
    private final ListenersFactory listenersFactory;
    private final RealList<RealType<?>> types;
    private final Operation operation;
    private ComputedValue value;

    public OperationOutput(Logger logger, ListenersFactory listenersFactory, RealList<RealType<?>> types, Operation operation) {
        super(listenersFactory);
        this.logger = logger;
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

        Value<TypeInstances, ?> firstValue = operation.getFirstValueSource().getValue();
        Value<TypeInstances, ?> secondValue = operation.getSecondValueSource().getValue();

        if(firstValue == null || secondValue == null) {
            if(value != null) {
                value = null;
                for(ValueAvailableListener listener : listeners)
                    listener.valueUnavailable(this);
            }
            return;
        }

        if(firstValue.getTypeId().equals(secondValue.getTypeId())) {
            RealType<Object> inputType = (RealType<Object>) types.get(firstValue.getTypeId());
            Operator<Object, Object> operator = (Operator<Object, Object>) operation.getOperatorsByType().get(inputType.getId());
            RealType<Object> outputType = (RealType<Object>) types.get(operator.getOutputTypeId());
            try {
                Object result = operator.apply(inputType.deserialise(firstValue.getValue().getElements().get(0)), inputType.deserialise(secondValue.getValue().getElements().get(0)));
                if(value != null && !value.getTypeId().equals(outputType.getId())) {
                    for(ValueAvailableListener listener : listeners)
                        listener.valueUnavailable(this);
                    value = null;
                }
                if(value == null)
                    value = new ComputedValue(listenersFactory, outputType);
                value.setTypeInstances(new TypeInstances(outputType.serialise(result)));
            } catch(Throwable t) {
                logger.error("Failed to operate on values", t);
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
    public Value<TypeInstances, ?> getValue() {
        return value;
    }

    @Override
    public void valueAvailable(ValueSource source, Value<TypeInstances, ?> value) {
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

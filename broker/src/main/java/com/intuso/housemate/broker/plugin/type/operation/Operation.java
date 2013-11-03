package com.intuso.housemate.broker.plugin.type.operation;

import com.intuso.housemate.broker.plugin.type.valuesource.ValueSource;
import com.intuso.housemate.plugin.api.OperationType;
import com.intuso.housemate.plugin.api.Operator;

import java.util.Map;

/**
 */
public class Operation {

    private final OperationType operationType;
    private final Map<String, Operator<?, ?>> operatorsByType;
    private final ValueSource firstValueSource;
    private final ValueSource secondValueSource;

    public Operation(OperationType operationType, Map<String, Operator<?, ?>> operatorsByType, ValueSource firstValueSource,
                     ValueSource secondValueSource) {
        this.operationType = operationType;
        this.operatorsByType = operatorsByType;
        this.firstValueSource = firstValueSource;
        this.secondValueSource = secondValueSource;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Map<String, Operator<?, ?>> getOperatorsByType() {
        return operatorsByType;
    }

    public ValueSource getFirstValueSource() {
        return firstValueSource;
    }

    public ValueSource getSecondValueSource() {
        return secondValueSource;
    }
}

package com.intuso.housemate.server.plugin.main.type.operation;

import com.intuso.housemate.plugin.api.internal.OperationType;
import com.intuso.housemate.plugin.api.internal.Operator;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;

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

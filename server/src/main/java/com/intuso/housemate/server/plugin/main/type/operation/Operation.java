package com.intuso.housemate.server.plugin.main.type.operation;

import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.plugin.api.internal.Operator;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;

import java.util.Map;

/**
 */
public class Operation {

    private final TypeInfo typeInfo;
    private final Map<String, Operator<?, ?>> operatorsByType;
    private final ValueSource firstValueSource;
    private final ValueSource secondValueSource;

    public Operation(TypeInfo typeInfo, Map<String, Operator<?, ?>> operatorsByType, ValueSource firstValueSource,
                     ValueSource secondValueSource) {
        this.typeInfo = typeInfo;
        this.operatorsByType = operatorsByType;
        this.firstValueSource = firstValueSource;
        this.secondValueSource = secondValueSource;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
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

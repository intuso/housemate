package com.intuso.housemate.server.plugin.main.type.comparison;

import com.intuso.housemate.plugin.api.internal.Comparator;
import com.intuso.housemate.plugin.api.internal.TypeInfo;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;

import java.util.Map;

/**
 */
public class Comparison {

    private final TypeInfo typeInfo;
    private final Map<String, Comparator<?>> comparatorsByType;
    private final ValueSource firstValueSource;
    private final ValueSource secondValueSource;

    public Comparison(TypeInfo typeInfo, Map<String, Comparator<?>> comparatorsByType, ValueSource firstValueSource,
                      ValueSource secondValueSource) {
        this.typeInfo = typeInfo;
        this.comparatorsByType = comparatorsByType;
        this.firstValueSource = firstValueSource;
        this.secondValueSource = secondValueSource;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public Map<String, Comparator<?>> getComparatorsByType() {
        return comparatorsByType;
    }

    public ValueSource getFirstValueSource() {
        return firstValueSource;
    }

    public ValueSource getSecondValueSource() {
        return secondValueSource;
    }
}

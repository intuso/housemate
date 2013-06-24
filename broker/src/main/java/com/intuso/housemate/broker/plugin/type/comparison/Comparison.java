package com.intuso.housemate.broker.plugin.type.comparison;

import com.intuso.housemate.broker.plugin.type.valuesource.ValueSource;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.ComparisonOperator;

import java.util.Map;

/**
 */
public class Comparison {

    private final ComparisonOperator operator;
    private final Map<String, Comparator<?>> comparatorsByType;
    private final ValueSource firstValueSource;
    private final ValueSource secondValueSource;

    public Comparison(ComparisonOperator operator, Map<String, Comparator<?>> comparatorsByType, ValueSource firstValueSource,
                      ValueSource secondValueSource) {
        this.operator = operator;
        this.comparatorsByType = comparatorsByType;
        this.firstValueSource = firstValueSource;
        this.secondValueSource = secondValueSource;
    }

    public ComparisonOperator getOperator() {
        return operator;
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

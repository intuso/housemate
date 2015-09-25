package com.intuso.housemate.server.plugin.main.type.comparison;

import com.intuso.housemate.plugin.api.internal.Comparator;
import com.intuso.housemate.plugin.api.internal.ComparisonType;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;

import java.util.Map;

/**
 */
public class Comparison {

    private final com.intuso.housemate.plugin.api.internal.ComparisonType comparisonType;
    private final Map<String, Comparator<?>> comparatorsByType;
    private final ValueSource firstValueSource;
    private final ValueSource secondValueSource;

    public Comparison(ComparisonType comparisonType, Map<String, Comparator<?>> comparatorsByType, ValueSource firstValueSource,
                      ValueSource secondValueSource) {
        this.comparisonType = comparisonType;
        this.comparatorsByType = comparatorsByType;
        this.firstValueSource = firstValueSource;
        this.secondValueSource = secondValueSource;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
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

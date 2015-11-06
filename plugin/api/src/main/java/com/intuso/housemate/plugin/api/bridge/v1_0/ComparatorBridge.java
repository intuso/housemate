package com.intuso.housemate.plugin.api.bridge.v1_0;

import com.intuso.housemate.plugin.api.internal.Comparator;
import com.intuso.housemate.plugin.api.internal.ComparisonType;

/**
 * Created by tomc on 06/11/15.
 */
public class ComparatorBridge<O> implements Comparator<O> {

    private final com.intuso.housemate.plugin.v1_0.api.Comparator<O> comparator;
    private final ComparisonType comparisonType;

    public ComparatorBridge(com.intuso.housemate.plugin.v1_0.api.Comparator<O> comparator) {
        this.comparator = comparator;
        this.comparisonType = new ComparisonTypeBridge(comparator.getComparisonType());
    }

    @Override
    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    @Override
    public String getTypeId() {
        return comparator.getTypeId();
    }

    @Override
    public boolean compare(O first, O second) {
        return comparator.compare(first, second);
    }
}

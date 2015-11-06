package com.intuso.housemate.plugin.api.bridge.v1_0;

import com.intuso.housemate.plugin.api.internal.ComparisonType;

/**
 * Created by tomc on 06/11/15.
 */
public class ComparisonTypeBridge implements ComparisonType {

    private final com.intuso.housemate.plugin.v1_0.api.ComparisonType comparisonType;

    public ComparisonTypeBridge(com.intuso.housemate.plugin.v1_0.api.ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    @Override
    public String getDescription() {
        return comparisonType.getDescription();
    }

    @Override
    public String getId() {
        return comparisonType.getId();
    }

    @Override
    public String getName() {
        return comparisonType.getName();
    }
}

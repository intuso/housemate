package com.intuso.housemate.client.api.bridge.v1_0.plugin;

import com.intuso.housemate.client.api.bridge.v1_0.annotation.IdBridge;
import com.intuso.housemate.client.v1_0.api.plugin.IntegerRangeType;

import java.lang.annotation.Annotation;

/**
 * Created by tomc on 06/11/15.
 */
public class IntegerRangeTypeBridge implements com.intuso.housemate.client.api.internal.plugin.IntegerRangeType {

    private final IntegerRangeType integerRangeType;

    public IntegerRangeTypeBridge(IntegerRangeType integerRangeType) {
        this.integerRangeType = integerRangeType;
    }

    @Override
    public com.intuso.housemate.client.api.internal.annotation.Id id() {
        return new IdBridge(integerRangeType.id());
    }

    @Override
    public int min() {
        return integerRangeType.min();
    }

    @Override
    public boolean minInclusive() {
        return integerRangeType.minInclusive();
    }

    @Override
    public int max() {
        return integerRangeType.max();
    }

    @Override
    public boolean maxInclusive() {
        return integerRangeType.maxInclusive();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return com.intuso.housemate.client.api.internal.plugin.IntegerRangeType.class;
    }
}

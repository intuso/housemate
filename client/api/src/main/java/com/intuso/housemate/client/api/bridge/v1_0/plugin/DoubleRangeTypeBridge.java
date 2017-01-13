package com.intuso.housemate.client.api.bridge.v1_0.plugin;

import com.intuso.housemate.client.api.bridge.v1_0.annotation.IdBridge;
import com.intuso.housemate.client.v1_0.api.plugin.DoubleRangeType;

import java.lang.annotation.Annotation;

/**
 * Created by tomc on 06/11/15.
 */
public class DoubleRangeTypeBridge implements com.intuso.housemate.client.api.internal.plugin.DoubleRangeType {

    private final DoubleRangeType doubleRangeType;

    public DoubleRangeTypeBridge(DoubleRangeType doubleRangeType) {
        this.doubleRangeType = doubleRangeType;
    }

    @Override
    public com.intuso.housemate.client.api.internal.annotation.Id id() {
        return new IdBridge(doubleRangeType.id());
    }

    @Override
    public double min() {
        return doubleRangeType.min();
    }

    @Override
    public boolean minInclusive() {
        return doubleRangeType.minInclusive();
    }

    @Override
    public double max() {
        return doubleRangeType.max();
    }

    @Override
    public boolean maxInclusive() {
        return doubleRangeType.maxInclusive();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return com.intuso.housemate.client.api.internal.plugin.DoubleRangeType.class;
    }
}

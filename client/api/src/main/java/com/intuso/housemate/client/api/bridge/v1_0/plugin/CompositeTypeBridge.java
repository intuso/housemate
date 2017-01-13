package com.intuso.housemate.client.api.bridge.v1_0.plugin;

import com.intuso.housemate.client.api.bridge.v1_0.annotation.IdBridge;
import com.intuso.housemate.client.v1_0.api.plugin.CompositeType;

import java.lang.annotation.Annotation;

/**
 * Created by tomc on 06/11/15.
 */
public class CompositeTypeBridge implements com.intuso.housemate.client.api.internal.plugin.CompositeType {

    private final CompositeType compositeType;

    public CompositeTypeBridge(CompositeType compositeType) {
        this.compositeType = compositeType;
    }

    @Override
    public com.intuso.housemate.client.api.internal.annotation.Id id() {
        return new IdBridge(compositeType.id());
    }

    @Override
    public Class<?> type() {
        return compositeType.type();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return com.intuso.housemate.client.api.internal.plugin.CompositeType.class;
    }
}

package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.intuso.housemate.plugin.v1_0.api.annotations.Id;

import java.lang.annotation.Annotation;

/**
 * Created by tomc on 06/11/15.
 */
public class IdBridge implements com.intuso.housemate.plugin.api.internal.annotations.Id {

    private final Id id;

    public IdBridge(Id id) {
        this.id = id;
    }

    @Override
    public String value() {
        return id.value();
    }

    @Override
    public String name() {
        return id.name();
    }

    @Override
    public String description() {
        return id.description();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return com.intuso.housemate.plugin.api.internal.annotations.Id.class;
    }
}

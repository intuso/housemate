package com.intuso.housemate.client.api.bridge.v1_0.annotation;

import com.intuso.housemate.client.v1_0.api.annotation.Id;

import java.lang.annotation.Annotation;

/**
 * Created by tomc on 06/11/15.
 */
public class IdBridge implements com.intuso.housemate.client.api.internal.annotation.Id {

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
        return com.intuso.housemate.client.api.internal.annotation.Id.class;
    }
}

package com.intuso.housemate.plugin.api.bridge.v1_0;

import com.intuso.housemate.plugin.v1_0.api.annotations.TypeInfo;

import java.lang.annotation.Annotation;

/**
 * Created by tomc on 06/11/15.
 */
public class TypeInfoBridge implements com.intuso.housemate.plugin.api.internal.annotations.TypeInfo {

    private final TypeInfo typeInfo;

    public TypeInfoBridge(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    @Override
    public String id() {
        return typeInfo.id();
    }

    @Override
    public String name() {
        return typeInfo.name();
    }

    @Override
    public String description() {
        return typeInfo.description();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return com.intuso.housemate.plugin.api.internal.annotations.TypeInfo.class;
    }
}

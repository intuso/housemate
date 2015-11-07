package com.intuso.housemate.plugin.api.internal;

/**
 * Created by tomc on 06/11/15.
 */
public interface PluginResource<RESOURCE> {
    TypeInfo getTypeInfo();
    RESOURCE getResource();
}

package com.intuso.housemate.plugin.api.internal;

import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;

/**
 * Created by tomc on 06/11/15.
 */
public interface PluginResource<RESOURCE> {
    TypeInfo getTypeInfo();
    RESOURCE getResource();
}

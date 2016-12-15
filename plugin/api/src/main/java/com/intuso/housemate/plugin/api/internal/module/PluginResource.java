package com.intuso.housemate.plugin.api.internal.module;

import com.intuso.housemate.plugin.api.internal.annotations.Id;

/**
 * Created by tomc on 06/11/15.
 */
public interface PluginResource<RESOURCE> {
    Id getId();
    RESOURCE getResource();
}

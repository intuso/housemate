package com.intuso.housemate.client.api.internal.plugin;

import com.intuso.housemate.client.api.internal.annotation.Id;

/**
 * Created by tomc on 06/11/15.
 */
public interface PluginResource<RESOURCE> {
    Id getId();
    RESOURCE getResource();
}

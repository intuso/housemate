package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.client.api.bridge.v1_0.annotation.IdBridge;
import com.intuso.housemate.client.api.internal.plugin.PluginResourceImpl;
import com.intuso.housemate.client.v1_0.api.plugin.PluginResource;

/**
 * Created by tomc on 06/11/15.
 */
public class PluginResourceMapper {

    public <FROM, TO> Function<PluginResource<FROM>, com.intuso.housemate.client.api.internal.plugin.PluginResource<TO>> getFromV1_0Function(final Function<? super FROM, ? extends TO> convertResource) {
        return new Function<PluginResource<FROM>, com.intuso.housemate.client.api.internal.plugin.PluginResource<TO>>() {
            @Override
            public com.intuso.housemate.client.api.internal.plugin.PluginResource<TO> apply(PluginResource<FROM> pluginResource) {
                return map(pluginResource, convertResource);
            }
        };
    }

    public <FROM, TO> com.intuso.housemate.client.api.internal.plugin.PluginResource<TO> map(PluginResource<FROM> pluginResource, Function<? super FROM, ? extends TO> convertResource) {
        return new PluginResourceImpl<>(
                new IdBridge(pluginResource.getId()),
                convertResource.apply(pluginResource.getResource()));
    }
}

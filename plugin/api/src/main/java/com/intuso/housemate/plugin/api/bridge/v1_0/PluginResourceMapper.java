package com.intuso.housemate.plugin.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.plugin.v1_0.api.PluginResource;

/**
 * Created by tomc on 06/11/15.
 */
public class PluginResourceMapper {

    public <FROM, TO> Function<PluginResource<FROM>, com.intuso.housemate.plugin.api.internal.PluginResource<TO>> getFromV1_0Function(final Function<? super FROM, ? extends TO> convertResource) {
        return new Function<PluginResource<FROM>, com.intuso.housemate.plugin.api.internal.PluginResource<TO>>() {
            @Override
            public com.intuso.housemate.plugin.api.internal.PluginResource<TO> apply(PluginResource<FROM> pluginResource) {
                return map(pluginResource, convertResource);
            }
        };
    }

    public <FROM, TO> com.intuso.housemate.plugin.api.internal.PluginResource<TO> map(PluginResource<FROM> pluginResource, Function<? super FROM, ? extends TO> convertResource) {
        return new com.intuso.housemate.plugin.api.internal.PluginResourceImpl<>(
                new TypeInfoBridge(pluginResource.getTypeInfo()),
                convertResource.apply(pluginResource.getResource()));
    }
}

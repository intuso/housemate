package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.client.v1_0.real.api.module.PluginResource;

/**
 * Created by tomc on 06/11/15.
 */
public class PluginResourceMapper {

    public <FROM, TO> Function<PluginResource<FROM>, com.intuso.housemate.client.real.api.internal.module.PluginResource<TO>> getFromV1_0Function(final Function<? super FROM, ? extends TO> convertResource) {
        return new Function<PluginResource<FROM>, com.intuso.housemate.client.real.api.internal.module.PluginResource<TO>>() {
            @Override
            public com.intuso.housemate.client.real.api.internal.module.PluginResource<TO> apply(PluginResource<FROM> pluginResource) {
                return map(pluginResource, convertResource);
            }
        };
    }

    public <FROM, TO> com.intuso.housemate.client.real.api.internal.module.PluginResource<TO> map(PluginResource<FROM> pluginResource, Function<? super FROM, ? extends TO> convertResource) {
        return new com.intuso.housemate.client.real.api.internal.module.PluginResourceImpl<>(
                new TypeInfoBridge(pluginResource.getTypeInfo()),
                convertResource.apply(pluginResource.getResource()));
    }
}

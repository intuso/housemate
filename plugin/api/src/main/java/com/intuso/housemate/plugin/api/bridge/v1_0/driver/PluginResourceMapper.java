package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.intuso.housemate.plugin.v1_0.api.driver.PluginResource;

/**
 * Created by tomc on 05/11/15.
 */
public class PluginResourceMapper {

    public <FROM, TO> Function<com.intuso.housemate.plugin.api.internal.driver.PluginResource<FROM>, PluginResource<TO>> getToV1_0Function(final Function<? super FROM, ? extends TO> convertFrom) {
        return new Function<com.intuso.housemate.plugin.api.internal.driver.PluginResource<FROM>, PluginResource<TO>>() {
            @Override
            public PluginResource<TO> apply(com.intuso.housemate.plugin.api.internal.driver.PluginResource<FROM> fromPluginResource) {
                return map(fromPluginResource, convertFrom);
            }
        };
    }

    public <FROM, TO> Function<PluginResource<FROM>, com.intuso.housemate.plugin.api.internal.driver.PluginResource<TO>> getFromV1_0Function(final Function<? super FROM, ? extends TO> convertFrom) {
        return new Function<PluginResource<FROM>, com.intuso.housemate.plugin.api.internal.driver.PluginResource<TO>>() {
            @Override
            public com.intuso.housemate.plugin.api.internal.driver.PluginResource<TO> apply(PluginResource<FROM> fromPluginResource) {
                return map(fromPluginResource, convertFrom);
            }
        };
    }

    public <FROM, TO> PluginResource<TO> map(com.intuso.housemate.plugin.api.internal.driver.PluginResource<FROM> pluginResource,
                                             Function<? super FROM, ? extends TO> convertFrom) {
        if(pluginResource == null)
            return null;
        else if(pluginResource instanceof PluginResourceBridge)
            return ((PluginResourceBridge<TO, FROM>)pluginResource).getPluginResource();
        return new PluginResourceBridgeReverse<>(pluginResource, convertFrom);
    }

    public <FROM, TO> com.intuso.housemate.plugin.api.internal.driver.PluginResource<TO> map(PluginResource<FROM> pluginResource,
                                             Function<? super FROM, ? extends TO> convertFrom) {
        if(pluginResource == null)
            return null;
        else if(pluginResource instanceof PluginResourceBridgeReverse)
            return ((PluginResourceBridgeReverse<TO, FROM>)pluginResource).getPluginResource();
        return new PluginResourceBridge<>(pluginResource, convertFrom);
    }
}

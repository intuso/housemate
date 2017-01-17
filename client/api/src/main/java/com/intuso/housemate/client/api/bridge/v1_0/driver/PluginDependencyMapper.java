package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.intuso.housemate.client.v1_0.api.driver.PluginDependency;

/**
 * Created by tomc on 05/11/15.
 */
public class PluginDependencyMapper {

    public <FROM, TO> Function<com.intuso.housemate.client.api.internal.driver.PluginDependency<FROM>, PluginDependency<TO>> getToV1_0Function(final Function<? super FROM, ? extends TO> convertFrom) {
        return new Function<com.intuso.housemate.client.api.internal.driver.PluginDependency<FROM>, PluginDependency<TO>>() {
            @Override
            public PluginDependency<TO> apply(com.intuso.housemate.client.api.internal.driver.PluginDependency fromPluginDependency) {
                return map(fromPluginDependency, convertFrom);
            }
        };
    }

    public <FROM, TO> Function<PluginDependency<FROM>, com.intuso.housemate.client.api.internal.driver.PluginDependency<TO>> getFromV1_0Function(final Function<? super FROM, ? extends TO> convertFrom) {
        return new Function<PluginDependency<FROM>, com.intuso.housemate.client.api.internal.driver.PluginDependency<TO>>() {
            @Override
            public com.intuso.housemate.client.api.internal.driver.PluginDependency apply(PluginDependency<FROM> fromPluginDependency) {
                return map(fromPluginDependency, convertFrom);
            }
        };
    }

    public <FROM, TO> PluginDependency<TO> map(com.intuso.housemate.client.api.internal.driver.PluginDependency<FROM> pluginDependency,
                                               Function<? super FROM, ? extends TO> convertFrom) {
        if(pluginDependency == null)
            return null;
        else if(pluginDependency instanceof PluginDependencyBridge)
            return ((PluginDependencyBridge<TO, FROM>) pluginDependency).getPluginDependency();
        return new PluginDependencyBridgeReverse<>(pluginDependency, convertFrom);
    }

    public <FROM, TO> com.intuso.housemate.client.api.internal.driver.PluginDependency<TO> map(PluginDependency<FROM> pluginDependency,
                                                                                               Function<? super FROM, ? extends TO> convertFrom) {
        if(pluginDependency == null)
            return null;
        else if(pluginDependency instanceof PluginDependencyBridgeReverse)
            return ((PluginDependencyBridgeReverse<TO, FROM>) pluginDependency).getPluginDependency();
        return new PluginDependencyBridge<>(pluginDependency, convertFrom);
    }
}

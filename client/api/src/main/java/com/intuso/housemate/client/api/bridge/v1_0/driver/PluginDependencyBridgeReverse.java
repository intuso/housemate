package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.intuso.housemate.client.v1_0.api.driver.PluginDependency;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * Created by tomc on 05/11/15.
 */
public class PluginDependencyBridgeReverse<FROM, TO> implements PluginDependency<TO> {

    private final com.intuso.housemate.client.api.internal.driver.PluginDependency<FROM> pluginDependency;
    private final Function<? super FROM, ? extends TO> convertFrom;

    public PluginDependencyBridgeReverse(com.intuso.housemate.client.api.internal.driver.PluginDependency pluginDependency, Function<? super FROM, ? extends TO> convertFrom) {
        this.pluginDependency = pluginDependency;
        this.convertFrom = convertFrom;
    }

    public com.intuso.housemate.client.api.internal.driver.PluginDependency getPluginDependency() {
        return pluginDependency;
    }

    @Override
    public TO getDependency() {
        return convertFrom.apply(pluginDependency.getDependency());
    }

    @Override
    public ManagedCollection.Registration addListener(Listener<TO> listener) {
        return pluginDependency.addListener(new ListenerBridge(listener));
    }

    private class ListenerBridge implements com.intuso.housemate.client.api.internal.driver.PluginDependency.Listener<FROM> {

        private final Listener<TO> listener;

        private ListenerBridge(Listener<TO> listener) {
            this.listener = listener;
        }

        @Override
        public void dependencyAvailable(FROM dependency) {
            listener.dependencyAvailable(convertFrom.apply(dependency));
        }

        @Override
        public void dependencyUnavailable() {
            listener.dependencyUnavailable();
        }
    }
}

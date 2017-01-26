package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.intuso.housemate.client.v1_0.api.driver.PluginDependency;
import com.intuso.utilities.listener.ManagedCollection;

/**
 * Created by tomc on 05/11/15.
 */
public class PluginDependencyBridge<FROM, TO> implements com.intuso.housemate.client.api.internal.driver.PluginDependency<TO> {

    private final PluginDependency<FROM> pluginDependency;
    private final Function<? super FROM, ? extends TO> convertFrom;

    public PluginDependencyBridge(PluginDependency<FROM> pluginDependency, Function<? super FROM, ? extends TO> convertFrom) {
        this.pluginDependency = pluginDependency;
        this.convertFrom = convertFrom;
    }

    public PluginDependency<FROM> getPluginDependency() {
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

    private class ListenerBridge implements PluginDependency.Listener<FROM> {

        private final Listener<TO> listener;

        private ListenerBridge(Listener<TO> listener) {
            this.listener = listener;
        }

        @Override
        public void dependencyAvailable(FROM resource) {
            listener.dependencyAvailable(convertFrom.apply(resource));
        }

        @Override
        public void dependencyUnavailable() {
            listener.dependencyUnavailable();
        }
    }
}

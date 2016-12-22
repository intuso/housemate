package com.intuso.housemate.client.api.internal.plugin;

import com.google.inject.Injector;

/**
 */
public interface PluginListener {
    void pluginAdded(Injector pluginInjector);
    void pluginRemoved(Injector pluginInjector);
}

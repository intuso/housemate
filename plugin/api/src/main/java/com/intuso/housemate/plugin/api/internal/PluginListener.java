package com.intuso.housemate.plugin.api.internal;

import com.google.inject.Injector;
import com.intuso.utilities.listener.Listener;

/**
 */
public interface PluginListener extends Listener {
    public void pluginAdded(Injector pluginInjector);
    public void pluginRemoved(Injector pluginInjector);
}

package com.intuso.housemate.server.plugin;

import com.google.inject.Injector;
import com.intuso.utilities.listener.Listener;

/**
 */
public interface PluginListener extends Listener {
    public void pluginAdded(Injector pluginInjector);
    public void pluginRemoved(Injector pluginInjector);
}

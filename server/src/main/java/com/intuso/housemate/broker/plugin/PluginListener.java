package com.intuso.housemate.broker.plugin;

import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.utilities.listener.Listener;

/**
 */
public interface PluginListener extends Listener {
    public void pluginAdded(PluginDescriptor plugin);
    public void pluginRemoved(PluginDescriptor plugin);
}

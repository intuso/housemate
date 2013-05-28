package com.intuso.housemate.broker;

import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.utilities.listener.Listener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/02/13
 * Time: 09:27
 * To change this template use File | Settings | File Templates.
 */
public interface PluginListener extends Listener {
    public void pluginAdded(PluginDescriptor plugin);
    public void pluginRemoved(PluginDescriptor plugin);
}

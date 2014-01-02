package com.intuso.housemate.server.plugin;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 12/11/13
 * Time: 09:13
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class PluginManager {

    private final Log log;
    private final RealResources realResources;

    private final List<PluginDescriptor> plugins = Lists.newArrayList();
    private final Listeners<PluginListener> pluginListeners = new Listeners<PluginListener>();

    @Inject
    public PluginManager(Log log, RealResources realResources) {
        this.log = log;
        this.realResources = realResources;
    }

    public void addPlugin(PluginDescriptor plugin) {
        log.d("New Plugin: " + plugin.getClass().getName());
        try {
            plugin.init(realResources);
        } catch(HousemateException e) {
            log.e("Failed to initialise plugin", e);
            return;
        }
        plugins.add(plugin);
        // some plugins add more plugin listeners, so need prevent concurrent modification
        for(PluginListener listener : Lists.newArrayList(pluginListeners))
            listener.pluginAdded(plugin);
    }

    public void removePlugin(PluginDescriptor plugin) {
        plugins.remove(plugin);
        for(PluginListener listener : pluginListeners)
            listener.pluginRemoved(plugin);
    }

    public ListenerRegistration addPluginListener(PluginListener listener, boolean callForExisting) {
        ListenerRegistration result = pluginListeners.addListener(listener);
        if(callForExisting)
            for(PluginDescriptor plugin : plugins)
                listener.pluginAdded(plugin);
        return result;
    }
}

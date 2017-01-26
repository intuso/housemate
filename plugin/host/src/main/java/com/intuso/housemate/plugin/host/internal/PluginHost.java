package com.intuso.housemate.plugin.host.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.plugin.Plugin;
import com.intuso.housemate.client.api.internal.plugin.PluginListener;
import com.intuso.utilities.listener.MemberRegistration;
import com.intuso.utilities.listener.ManagedCollection;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 12/11/13
 * Time: 09:13
 * To change this template use File | Settings | File Templates.
 */
public class PluginHost extends AbstractIdleService implements PluginFileFinder.Listener {

    private final static Logger logger = LoggerFactory.getLogger(PluginHost.class);

    private final PluginFileFinder pluginFileFinder;
    private MemberRegistration pluginFinderListenerRegistration;
    private final ManagedCollection<PluginListener> internalListeners;
    private final ManagedCollection<com.intuso.housemate.client.v1_0.api.plugin.PluginListener> v1_0Listeners;
    private final Map<String, Plugins> loadedPlugins = Maps.newHashMap();

    @Inject
    public PluginHost(ManagedCollectionFactory managedCollectionFactory, PluginFileFinder pluginFileFinder,
                      Set<PluginListener> internalListeners,
                      Set<com.intuso.housemate.client.v1_0.api.plugin.PluginListener> v1_0Listeners) {
        this.pluginFileFinder = pluginFileFinder;
        this.internalListeners = managedCollectionFactory.create();
        for(PluginListener listener : internalListeners)
            this.internalListeners.add(listener);
        this.v1_0Listeners = managedCollectionFactory.create();
        for(com.intuso.housemate.client.v1_0.api.plugin.PluginListener listener : v1_0Listeners)
            this.v1_0Listeners.add(listener);
    }

    @Override
    public synchronized void startUp() {
        pluginFinderListenerRegistration = pluginFileFinder.addListener(this, true);
    }

    @Override
    public synchronized void shutDown() {
        if(pluginFinderListenerRegistration != null) {
            pluginFinderListenerRegistration.removeListener();
            pluginFinderListenerRegistration = null;
        }
        for(String id : Sets.newHashSet(loadedPlugins.keySet()))
            fileRemoved(id);
    }

    @Override
    public String fileFound(File pluginFile) {
        logger.debug("Loading plugins from " + pluginFile.getAbsolutePath());
        try {
            ClassLoader cl = new URLClassLoader(new URL[] {pluginFile.toURI().toURL()}, getClass().getClassLoader());
            Plugins plugins = new Plugins(pluginFile.getAbsolutePath(), cl);
            for(Plugin plugin : plugins.internalPlugins)
                for(PluginListener pluginListener : internalListeners)
                    pluginListener.pluginAdded(plugin);
            for(com.intuso.housemate.client.v1_0.api.plugin.Plugin plugin : plugins.v1_0Plugins)
                for(com.intuso.housemate.client.v1_0.api.plugin.PluginListener pluginListener : v1_0Listeners)
                    pluginListener.pluginAdded(plugin);
        } catch(IOException e) {
            logger.error("Failed to load  plugin from " + pluginFile.getAbsolutePath() + ". Could not get URL for file");
        }
        return UUID.randomUUID().toString();
    }

    @Override
    public void fileRemoved(String id) {
        Plugins plugins = loadedPlugins.remove(id);
        if(plugins != null) {
            logger.debug("Unloading plugins from removed file " + plugins.filePath);
            for(Plugin plugin : plugins.internalPlugins)
                for(PluginListener pluginListener : internalListeners)
                    pluginListener.pluginRemoved(plugin);
            for(com.intuso.housemate.client.v1_0.api.plugin.Plugin plugin : plugins.v1_0Plugins)
                for(com.intuso.housemate.client.v1_0.api.plugin.PluginListener pluginListener : v1_0Listeners)
                    pluginListener.pluginRemoved(plugin);
        }
    }

    private class Plugins {

        private final String filePath;
        private final List<Plugin> internalPlugins;
        private final List<com.intuso.housemate.client.v1_0.api.plugin.Plugin> v1_0Plugins;

        public Plugins(String filePath, ClassLoader cl) {
            this.filePath = filePath;
            internalPlugins = Lists.newArrayList(ServiceLoader.load(Plugin.class, cl));
            v1_0Plugins = Lists.newArrayList(ServiceLoader.load(com.intuso.housemate.client.v1_0.api.plugin.Plugin.class, cl));
        }
    }
}

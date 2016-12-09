package com.intuso.housemate.plugin.host.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.intuso.housemate.client.real.api.bridge.v1_0.ioc.PluginV1_0BridgeModule;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.module.PluginListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 12/11/13
 * Time: 09:13
 * To change this template use File | Settings | File Templates.
 */
public class PluginHost implements PluginFinder.Listener {

    private final static Logger logger = LoggerFactory.getLogger(PluginHost.class);

    private final Injector injector;
    private final PluginFinder pluginFinder;
    private ListenerRegistration pluginFinderListenerRegistration;
    private final Listeners<PluginListener> pluginListeners;
    private final Map<String, Injector> pluginInjectors = Maps.newHashMap();

    @Inject
    public PluginHost(Injector injector, ListenersFactory listenersFactory, PluginFinder pluginFinder) {
        this.injector = injector;
        this.pluginFinder = pluginFinder;
        this.pluginListeners = listenersFactory.create();
    }

    public synchronized void start() {
        stop();
        pluginFinderListenerRegistration = pluginFinder.addListener(this, true);
    }

    public synchronized void stop() {
        if(pluginFinderListenerRegistration != null) {
            pluginFinderListenerRegistration.removeListener();
            pluginFinderListenerRegistration = null;
        }
        for(String id : Sets.newHashSet(pluginInjectors.keySet()))
            pluginRemoved(id);
    }

    public ListenerRegistration addPluginListener(PluginListener listener, boolean callForExisting) {
        ListenerRegistration result = pluginListeners.addListener(listener);
        if(callForExisting)
            for(Injector pluginInjector : pluginInjectors.values())
                listener.pluginAdded(pluginInjector);
        return result;
    }

    @Override
    public String pluginFound(File pluginFile) {
        try {
            return addPlugin(createPluginInjector(injector, pluginFile, logger));
        } catch(Throwable t) {
            logger.warn("Failed to add plugin for file " + pluginFile.getAbsolutePath(), t);
            return null;
        }
    }

    public String addPlugin(Injector pluginInjector) {

        logger.debug("Adding plugin");

        Version version = detectVersion(pluginInjector);
        pluginInjector = version.createChildInjector(pluginInjector);

        TypeInfo typeInfo = pluginInjector.getInstance(TypeInfo.class);

        // some plugins add more plugin listeners, so need prevent concurrent modification
        for(PluginListener listener : Lists.newArrayList(pluginListeners))
            listener.pluginAdded(pluginInjector);

        pluginInjectors.put(typeInfo.id(), pluginInjector);
        return typeInfo.id();
    }

    @Override
    public void pluginRemoved(String id) {
        logger.debug("Removing plugin : " + id);
        Injector pluginInjector = pluginInjectors.remove(id);
        if(pluginInjector != null)
            for(PluginListener listener : pluginListeners)
                listener.pluginRemoved(pluginInjector);
    }

    private Injector createPluginInjector(Injector injector, File file, Logger logger) {
        return injector.createChildInjector(getPluginModules(file, logger));
    }

    private List<Module> getPluginModules(File file, Logger logger) {
        logger.debug("Loading plugins from " + file.getAbsolutePath());
        try {
            ClassLoader cl = new URLClassLoader(new URL[] {file.toURI().toURL()}, getClass().getClassLoader());
            return Lists.newArrayList(ServiceLoader.load(Module.class, cl));
        } catch(MalformedURLException e) {
            logger.error("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not get URL for file");
            return Lists.newArrayList();
        } catch(IOException e) {
            logger.error("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not load mainifest file");
            return Lists.newArrayList();
        }
    }

    private Version detectVersion(Injector pluginInjector) {
        try {
            pluginInjector.getInstance(TypeInfo.class);
            return Version.Internal;
        } catch(Throwable t) {}
        try {
            pluginInjector.getInstance(com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo.class);
            return Version.V1_0;
        } catch(Throwable t) {}
        throw new HousematePluginException("Could not detect plugin api version");
    }

    enum Version {
        Internal {
            @Override
            public Injector createChildInjector(Injector injector) {
                return injector;
            }
        },
        V1_0 {
            @Override
            public Injector createChildInjector(Injector injector) {
                return injector.createChildInjector(new PluginV1_0BridgeModule());
            }
        };

        public abstract Injector createChildInjector(Injector injector);
    }
}

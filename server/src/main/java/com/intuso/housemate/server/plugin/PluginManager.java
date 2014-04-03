package com.intuso.housemate.server.plugin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.plugin.api.PluginModule;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 12/11/13
 * Time: 09:13
 * To change this template use File | Settings | File Templates.
 */
public class PluginManager {

    private final Log log;
    private final Injector injector;

    private final Map<Class<? extends PluginModule>, Injector> pluginInjectors = Maps.newHashMap();
    private final Listeners<PluginListener> pluginListeners;

    @Inject
    public PluginManager(Log log, ListenersFactory listenersFactory, Injector injector) {
        this.log = log;
        this.injector = injector;
        this.pluginListeners = listenersFactory.create();
    }

    public void addPlugin(Class<? extends PluginModule> pluginModuleClass) {
        log.d("New Plugin module : " + pluginModuleClass.getName());
        Injector pluginInjector = injector.createChildInjector(injector.getInstance(pluginModuleClass));
        pluginInjectors.put(pluginModuleClass, pluginInjector);
        // some plugins add more plugin listeners, so need prevent concurrent modification
        for(PluginListener listener : Lists.newArrayList(pluginListeners))
            listener.pluginAdded(pluginInjector);
    }

    public void removePlugin(Class<? extends PluginModule> pluginModuleClass) {
        Injector pluginInjector = pluginInjectors.remove(pluginModuleClass);
        if(pluginInjector != null)
            for(PluginListener listener : pluginListeners)
                listener.pluginRemoved(pluginInjector);
    }

    public ListenerRegistration addPluginListener(PluginListener listener, boolean callForExisting) {
        ListenerRegistration result = pluginListeners.addListener(listener);
        if(callForExisting)
            for(Injector pluginInjector : pluginInjectors.values())
                listener.pluginAdded(pluginInjector);
        return result;
    }
}

package com.intuso.housemate.platform.pc;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.plugin.host.internal.PluginFinder;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by tomc on 21/11/16.
 */
public class PCPluginFinder implements PluginFinder {

    public final static String PLUGINS_DIR_NAME= "plugins";

    private final static Logger logger = LoggerFactory.getLogger(PCPluginFinder.class);

    private final Map<File, WeakHashMap<Listener, String>> listenerPluginIds = Maps.newHashMap();
    private final Listeners<Listener> listeners;

    @Inject
    public PCPluginFinder(ListenersFactory listenersFactory, PropertyRepository properties) {
        this.listeners = listenersFactory.create();
        File pluginDirectory = new File(properties.get(Properties.HOUSEMATE_CONFIG_DIR) + File.separator + PLUGINS_DIR_NAME);
        if(!pluginDirectory.exists())
            pluginDirectory.mkdir();
        if(pluginDirectory.isFile())
            logger.warn("Plugin path is not a directory");
        else {
            logger.debug("Loading plugins from " + pluginDirectory.getAbsolutePath());
            for(File pluginFile : pluginDirectory.listFiles(new PluginFileFilter()))
                listenerPluginIds.put(pluginFile, new WeakHashMap<Listener, String>());
        }
    }

    @Override
    public ListenerRegistration addListener(Listener listener, boolean callForExisting) {
        ListenerRegistration result = listeners.addListener(listener);
        for(File pluginFile : listenerPluginIds.keySet())
            listenerPluginIds.get(pluginFile).put(listener, listener.pluginFound(pluginFile));
        return result;
    }

    private class PluginFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return file.isFile() && file.getName().endsWith(".jar");
        }
    }
}

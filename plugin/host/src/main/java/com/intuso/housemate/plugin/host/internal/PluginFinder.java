package com.intuso.housemate.plugin.host.internal;

import com.intuso.utilities.listener.ListenerRegistration;

import java.io.File;

/**
 * Created by tomc on 21/11/16.
 */
public interface PluginFinder {
    ListenerRegistration addListener(Listener listener, boolean callForExisting);

    interface Listener extends com.intuso.utilities.listener.Listener {
        String pluginFound(File pluginFile);
        void pluginRemoved(String pluginId);
    }
}

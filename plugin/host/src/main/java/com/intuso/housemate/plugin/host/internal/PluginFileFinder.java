package com.intuso.housemate.plugin.host.internal;

import com.intuso.utilities.listener.ListenerRegistration;

import java.io.File;

/**
 * Created by tomc on 21/11/16.
 */
public interface PluginFileFinder {

    ListenerRegistration addListener(Listener listener, boolean callForExisting);

    interface Listener {
        String fileFound(File pluginFile);
        void fileRemoved(String pluginId);
    }
}

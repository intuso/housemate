package com.intuso.housemate.plugin.host.internal;

import com.intuso.utilities.collection.ManagedCollection;

import java.io.File;

/**
 * Created by tomc on 21/11/16.
 */
public interface PluginFileFinder {

    ManagedCollection.Registration addListener(Listener listener, boolean callForExisting);

    interface Listener {
        String fileFound(File pluginFile);
        void fileRemoved(String pluginId);
    }
}

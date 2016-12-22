package com.intuso.housemate.client.api.internal.driver;

import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 29/10/15.
 */
public interface PluginDependency<DEPENDENCY> {

    DEPENDENCY getDependency();

    ListenerRegistration addListener(Listener<DEPENDENCY> listener);

    interface Listener<DEPENDENCY> {
        void dependencyAvailable(DEPENDENCY DEPENDENCY);
        void dependencyUnavailable();
    }
}

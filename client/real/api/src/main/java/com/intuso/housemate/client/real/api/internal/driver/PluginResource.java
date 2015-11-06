package com.intuso.housemate.client.real.api.internal.driver;

import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 29/10/15.
 */
public interface PluginResource<RESOURCE> {

    RESOURCE getResource();

    ListenerRegistration addListener(Listener<RESOURCE> listener);

    interface Listener<RESOURCE> extends com.intuso.utilities.listener.Listener {
        void resourceAvailable(RESOURCE resource);
        void resourceUnavailable();
    }
}

package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.driver.FeatureDriver;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

@Id(value = "test-feature", name = "Test Feature", description = "Test Feature")
public class TestFeatureDriver implements FeatureDriver, TestFeature {

    private final Listeners<Listener> listeners;

    @Inject
    public TestFeatureDriver(@Assisted Logger logger,
                             @Assisted FeatureDriver.Callback callback,
                             ListenersFactory listenersFactory) {
        this.listeners = listenersFactory.create();
    }

    @Override
    public void startFeature() {
        // init stuff here
    }

    @Override
    public void stopFeature() {
        // uninit stuff here
    }

    @Override
    public void setValue(double value) {
        for(Listener listener : listeners)
            listener.doubleValue(value);
    }

    @Override
    public ListenerRegistration addListener(Listener listener) {
        return listeners.addListener(listener);
    }

}

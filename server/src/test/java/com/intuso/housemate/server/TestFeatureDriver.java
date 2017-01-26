package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.driver.FeatureDriver;
import com.intuso.utilities.listener.ManagedCollection;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

@Id(value = "test-feature", name = "Test Feature", description = "Test Feature")
public class TestFeatureDriver implements FeatureDriver, TestFeature {

    private final ManagedCollection<Listener> listeners;

    @Inject
    public TestFeatureDriver(ManagedCollectionFactory managedCollectionFactory) {
        this.listeners = managedCollectionFactory.create();
    }

    @Override
    public void init(Logger logger, FeatureDriver.Callback callback) {
        // init stuff here
    }

    @Override
    public void uninit() {
        // uninit stuff here
    }

    @Override
    public void setValue(double value) {
        for(Listener listener : listeners)
            listener.doubleValue(value);
    }

    @Override
    public ManagedCollection.Registration addListener(Listener listener) {
        return listeners.add(listener);
    }

}

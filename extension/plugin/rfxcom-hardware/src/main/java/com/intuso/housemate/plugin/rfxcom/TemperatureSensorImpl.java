package com.intuso.housemate.plugin.rfxcom;

import com.intuso.housemate.client.v1_0.api.api.TemperatureSensor;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;

/**
 * Created by tomc on 02/02/17.
 */
public class TemperatureSensorImpl implements TemperatureSensor {

    private final ManagedCollection<Listener> listeners;

    protected TemperatureSensorImpl(ManagedCollectionFactory managedCollectionFactory) {
        this.listeners = managedCollectionFactory.create();
    }

    @Override
    public ManagedCollection.Registration addListener(Listener listener) {
        return listeners.add(listener);
    }

    public void setTemperature(double temperature) {
        for(Listener listener : listeners)
            listener.temperature(temperature);
    }
}

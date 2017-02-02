package com.intuso.housemate.plugin.rfxcom;

import com.intuso.housemate.client.v1_0.api.feature.PowerControl;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;

/**
 * Created by tomc on 02/02/17.
 */
public abstract class PowerControlBase implements PowerControl {

    private final ManagedCollection<Listener> listeners;

    protected PowerControlBase(ManagedCollectionFactory managedCollectionFactory) {
        this.listeners = managedCollectionFactory.create();
    }

    @Override
    public ManagedCollection.Registration addListener(Listener listener) {
        return listeners.add(listener);
    }

    public void setOn(boolean on) {
        for(Listener listener : listeners)
            listener.on(on);
    }
}

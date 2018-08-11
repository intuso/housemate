package com.intuso.housemate.plugin.rfxcom;

import com.intuso.housemate.client.v1_0.api.ability.Power;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;

/**
 * Created by tomc on 02/02/17.
 */
public abstract class PowerBase implements Power.Control, Power.State {

    private final ManagedCollection<Listener> listeners;

    private Boolean on;

    protected PowerBase(ManagedCollectionFactory managedCollectionFactory) {
        this.listeners = managedCollectionFactory.createSet();
    }

    @Override
    public synchronized ManagedCollection.Registration addListener(Listener listener) {
        listener.on(on);
        return listeners.add(listener);
    }

    public synchronized void setOn(boolean on) {
        this.on = on;
        for(Listener listener : listeners)
            listener.on(on);
    }
}

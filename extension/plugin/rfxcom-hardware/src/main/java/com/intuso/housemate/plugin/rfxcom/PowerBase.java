package com.intuso.housemate.plugin.rfxcom;

import com.intuso.housemate.client.v1_0.api.ability.Power;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;

/**
 * Created by tomc on 02/02/17.
 */
public abstract class PowerBase implements Power {

    private final ManagedCollection<Listener> listeners;

    protected PowerBase(ManagedCollectionFactory managedCollectionFactory) {
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

package com.intuso.housemate.server.storage.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.hardware.Hardware;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:24
* To change this template use File | Settings | File Templates.
*/
public class HardwareListWatcher implements ListListener<Hardware<?, ?>> {

    private final Multimap<Hardware<?, ?>, ListenerRegistration> listeners = HashMultimap.create();

    private final Log log;
    private final Persistence persistence;
    private final PropertyListWatcher propertyListWatcher;

    @Inject
    public HardwareListWatcher(Log log, Persistence persistence, ValueWatcher valueWatcher, PropertyListWatcher propertyListWatcher) {
        this.log = log;
        this.persistence = persistence;
        this.propertyListWatcher = propertyListWatcher;
    }

    @Override
    public void elementAdded(Hardware<?, ?> hardware) {
        listeners.put(hardware, hardware.getProperties().addObjectListener(propertyListWatcher, true));
    }

    @Override
    public void elementRemoved(Hardware<?, ?> hardware) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(hardware);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
        try {
            persistence.removeValues(hardware.getPath());
        } catch(HousemateException e) {
            log.e("Failed to delete hardware properties", e);
        }
    }
}

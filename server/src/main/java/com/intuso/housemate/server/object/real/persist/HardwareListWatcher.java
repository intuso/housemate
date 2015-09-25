package com.intuso.housemate.server.object.real.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.persistence.api.internal.Persistence;
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
public class HardwareListWatcher implements List.Listener<RealHardware> {

    private final Multimap<RealHardware, ListenerRegistration> listeners = HashMultimap.create();

    private final Log log;
    private final Persistence persistence;
    private final PropertyListWatcher propertyListWatcher;

    @Inject
    public HardwareListWatcher(Log log, Persistence persistence, ValueBaseWatcher valueBaseWatcher, PropertyListWatcher propertyListWatcher) {
        this.log = log;
        this.persistence = persistence;
        this.propertyListWatcher = propertyListWatcher;
    }

    @Override
    public void elementAdded(RealHardware hardware) {

        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.getChildren().put("id", new TypeInstances(new TypeInstance(hardware.getId())));
        toSave.getChildren().put("name", new TypeInstances(new TypeInstance(hardware.getName())));
        toSave.getChildren().put("description", new TypeInstances(new TypeInstance(hardware.getDescription())));
        toSave.getChildren().put("type", new TypeInstances(new TypeInstance(hardware.getType())));
        try {
            persistence.saveValues(hardware.getPath(), toSave);
        } catch (Throwable t) {
            log.e("Failed to save new hardware values", t);
        }

        listeners.put(hardware, hardware.getProperties().addObjectListener(propertyListWatcher, true));
    }

    @Override
    public void elementRemoved(RealHardware hardware) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(hardware);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
        try {
            persistence.removeValues(hardware.getPath());
        } catch(Throwable t) {
            log.e("Failed to delete hardware properties", t);
        }
    }
}

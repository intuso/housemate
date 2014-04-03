package com.intuso.housemate.server.storage.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.server.storage.DetailsNotFoundException;
import com.intuso.housemate.server.storage.Storage;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:25
* To change this template use File | Settings | File Templates.
*/
public class PropertyListWatcher implements ListListener<Property<?, ?, ?>> {

    private final Multimap<Property<?, ?, ?>, ListenerRegistration> listeners = HashMultimap.create();

    private final Log log;
    private final Storage storage;
    private final ValueWatcher valueWatcher;

    @Inject
    public PropertyListWatcher(Log log, Storage storage, ValueWatcher valueWatcher) {
        this.log = log;
        this.storage = storage;
        this.valueWatcher = valueWatcher;
    }

    @Override
    public void elementAdded(Property<?, ?, ?> property) {
        try {
            property.set(storage.getTypeInstances(property.getPath()),
                    new CommandPerformListener(log, "Set property value " + Arrays.toString(property.getPath())));
        } catch(DetailsNotFoundException e) {
            log.w("No details found for property value " + Arrays.toString(property.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to set property value " + Arrays.toString(property.getPath()), e);
        }
        listeners.put(property, property.addObjectListener(valueWatcher));
        valueWatcher.valueChanged(property);
    }

    @Override
    public void elementRemoved(Property<?, ?, ?> property) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(property);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
    }
}

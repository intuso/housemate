package com.intuso.housemate.server.object.real.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:25
* To change this template use File | Settings | File Templates.
*/
public class PropertyListWatcher implements ListListener<RealProperty<?>> {

    private final Multimap<RealProperty, ListenerRegistration> listeners = HashMultimap.create();

    private final ValueWatcher valueWatcher;

    @Inject
    public PropertyListWatcher(ValueWatcher valueWatcher) {
        this.valueWatcher = valueWatcher;
    }

    @Override
    public void elementAdded(RealProperty<?> property) {
        listeners.put(property, valueWatcher.watch(property));
    }

    @Override
    public void elementRemoved(RealProperty<?> property) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(property);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
    }
}

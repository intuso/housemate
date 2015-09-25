package com.intuso.housemate.server.object.real.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:25
* To change this template use File | Settings | File Templates.
*/
public class PropertyListWatcher implements List.Listener<RealProperty<?>> {

    private final Multimap<RealProperty, ListenerRegistration> listeners = HashMultimap.create();

    private final ValueBaseWatcher valueBaseWatcher;

    @Inject
    public PropertyListWatcher(ValueBaseWatcher valueBaseWatcher) {
        this.valueBaseWatcher = valueBaseWatcher;
    }

    @Override
    public void elementAdded(RealProperty<?> property) {
        listeners.put(property, valueBaseWatcher.watch(property));
    }

    @Override
    public void elementRemoved(RealProperty<?> property) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(property);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
    }
}

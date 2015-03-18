package com.intuso.housemate.server.storage.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.realclient.storage.persist.PropertyListWatcher;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:24
* To change this template use File | Settings | File Templates.
*/
public class ConditionListWatcher implements ListListener<Condition<?, ?, ?, ?, ?, ?, ?>> {

    private final Multimap<Condition<?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> listeners = HashMultimap.create();

    private final PropertyListWatcher propertyListWatcher;
    private ConditionListWatcher conditionListWatcher; // cannot init this in constructor as we'll get inifite recursion

    @Inject
    public ConditionListWatcher(PropertyListWatcher propertyListWatcher) {
        this.propertyListWatcher = propertyListWatcher;
    }

    @Override
    public void elementAdded(Condition<?, ?, ?, ?, ?, ?, ?> condition) {
        listeners.put(condition, condition.getProperties().addObjectListener(propertyListWatcher, true));
        if(conditionListWatcher == null)
            conditionListWatcher = new ConditionListWatcher(propertyListWatcher);
        listeners.put(condition, condition.getConditions().addObjectListener(conditionListWatcher, true));
    }

    @Override
    public void elementRemoved(Condition<?, ?, ?, ?, ?, ?, ?> condition) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(condition);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
    }
}

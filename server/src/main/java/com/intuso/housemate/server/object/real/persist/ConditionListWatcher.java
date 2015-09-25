package com.intuso.housemate.server.object.real.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealCondition;
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
public class ConditionListWatcher implements List.Listener<RealCondition> {

    private final Multimap<RealCondition, ListenerRegistration> listeners = HashMultimap.create();

    private final Log log;
    private final Persistence persistence;
    private final PropertyListWatcher propertyListWatcher;
    private ConditionListWatcher conditionListWatcher; // cannot init this in constructor as we'll get inifite recursion

    @Inject
    public ConditionListWatcher(Log log, Persistence persistence, PropertyListWatcher propertyListWatcher) {
        this.log = log;
        this.persistence = persistence;
        this.propertyListWatcher = propertyListWatcher;
    }

    @Override
    public void elementAdded(RealCondition condition) {

        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.getChildren().put("id", new TypeInstances(new TypeInstance(condition.getId())));
        toSave.getChildren().put("name", new TypeInstances(new TypeInstance(condition.getName())));
        toSave.getChildren().put("description", new TypeInstances(new TypeInstance(condition.getDescription())));
        toSave.getChildren().put("type", new TypeInstances(new TypeInstance(condition.getType())));
        try {
            persistence.saveValues(condition.getPath(), toSave);
        } catch (Throwable t) {
            log.e("Failed to save new automation values", t);
        }

        listeners.put(condition, condition.getProperties().addObjectListener(propertyListWatcher, true));
        if(conditionListWatcher == null)
            conditionListWatcher = new ConditionListWatcher(log, persistence, propertyListWatcher);
        listeners.put(condition, condition.getConditions().addObjectListener(conditionListWatcher, true));
    }

    @Override
    public void elementRemoved(RealCondition condition) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(condition);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
    }
}
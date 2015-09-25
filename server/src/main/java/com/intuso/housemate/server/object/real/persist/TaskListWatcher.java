package com.intuso.housemate.server.object.real.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealTask;
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
* Time: 19:25
* To change this template use File | Settings | File Templates.
*/
public class TaskListWatcher implements List.Listener<RealTask> {

    private final Multimap<RealTask, ListenerRegistration> listeners = HashMultimap.create();

    private final Log log;
    private final Persistence persistence;
    private final PropertyListWatcher propertyListWatcher;

    @Inject
    public TaskListWatcher(Log log, Persistence persistence, PropertyListWatcher propertyListWatcher) {
        this.log = log;
        this.persistence = persistence;
        this.propertyListWatcher = propertyListWatcher;
    }

    @Override
    public void elementAdded(RealTask task) {

        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.getChildren().put("id", new TypeInstances(new TypeInstance(task.getId())));
        toSave.getChildren().put("name", new TypeInstances(new TypeInstance(task.getName())));
        toSave.getChildren().put("description", new TypeInstances(new TypeInstance(task.getDescription())));
        toSave.getChildren().put("type", new TypeInstances(new TypeInstance(task.getType())));
        try {
            persistence.saveValues(task.getPath(), toSave);
        } catch (Throwable t) {
            log.e("Failed to save new automation values", t);
        }

        listeners.put(task, task.getProperties().addObjectListener(propertyListWatcher, true));
    }

    @Override
    public void elementRemoved(RealTask task) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(task);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
    }
}

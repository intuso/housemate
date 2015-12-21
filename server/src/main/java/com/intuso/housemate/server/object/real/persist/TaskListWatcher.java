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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:25
* To change this template use File | Settings | File Templates.
*/
public class TaskListWatcher implements List.Listener<RealTask<?>> {

    private final static Logger logger = LoggerFactory.getLogger(TaskListWatcher.class);

    private final Multimap<RealTask, ListenerRegistration> listeners = HashMultimap.create();
    private final Persistence persistence;
    private final ValueBaseWatcher valueBaseWatcher;
    private final PropertyListWatcher propertyListWatcher;

    @Inject
    public TaskListWatcher(Persistence persistence, ValueBaseWatcher valueBaseWatcher, PropertyListWatcher propertyListWatcher) {
        this.persistence = persistence;
        this.valueBaseWatcher = valueBaseWatcher;
        this.propertyListWatcher = propertyListWatcher;
    }

    @Override
    public void elementAdded(RealTask<?> task) {

        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.getChildren().put("id", new TypeInstances(new TypeInstance(task.getId())));
        toSave.getChildren().put("name", new TypeInstances(new TypeInstance(task.getName())));
        toSave.getChildren().put("description", new TypeInstances(new TypeInstance(task.getDescription())));
        try {
            persistence.saveValues(task.getPath(), toSave);
        } catch (Throwable t) {
            logger.error("Failed to save new automation values", t);
        }
        listeners.put(task, valueBaseWatcher.watch(task.getDriverProperty()));
        listeners.put(task, task.getProperties().addObjectListener(propertyListWatcher, true));
    }

    @Override
    public void elementRemoved(RealTask<?> task) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(task);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
    }
}

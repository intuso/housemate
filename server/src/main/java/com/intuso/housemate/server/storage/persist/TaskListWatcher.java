package com.intuso.housemate.server.storage.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:25
* To change this template use File | Settings | File Templates.
*/
public class TaskListWatcher implements ListListener<Task<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>> {

    private final Multimap<Task<?, ?, ?, ?, ?>, ListenerRegistration> listeners = HashMultimap.create();

    private final PropertyListWatcher propertyListWatcher;

    @Inject
    public TaskListWatcher(PropertyListWatcher propertyListWatcher) {
        this.propertyListWatcher = propertyListWatcher;
    }

    @Override
    public void elementAdded(Task<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?> task) {
        listeners.put(task, task.getProperties().addObjectListener(propertyListWatcher, true));
    }

    @Override
    public void elementRemoved(Task<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?> task) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(task);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
    }
}

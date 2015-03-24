package com.intuso.housemate.realclient.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.object.real.RealTask;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:25
* To change this template use File | Settings | File Templates.
*/
public class TaskListWatcher implements ListListener<RealTask> {

    private final Multimap<RealTask, ListenerRegistration> listeners = HashMultimap.create();

    private final PropertyListWatcher propertyListWatcher;

    @Inject
    public TaskListWatcher(PropertyListWatcher propertyListWatcher) {
        this.propertyListWatcher = propertyListWatcher;
    }

    @Override
    public void elementAdded(RealTask task) {
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

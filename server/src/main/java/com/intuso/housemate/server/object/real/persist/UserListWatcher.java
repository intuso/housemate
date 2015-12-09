package com.intuso.housemate.server.object.real.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealUser;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.utilities.listener.ListenerRegistration;
import org.slf4j.Logger;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/02/14
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */
public class UserListWatcher implements List.Listener<RealUser> {

    private final Multimap<RealUser, ListenerRegistration> listeners = HashMultimap.create();

    private final Logger logger;
    private final Persistence persistence;
    private final ValueBaseWatcher valueBaseWatcher;

    @Inject
    public UserListWatcher(Logger logger, Persistence persistence, ValueBaseWatcher valueBaseWatcher) {
        this.logger = logger;
        this.persistence = persistence;
        this.valueBaseWatcher = valueBaseWatcher;
    }

    @Override
    public void elementAdded(RealUser user) {
        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.getChildren().put("id", new TypeInstances(new TypeInstance(user.getId())));
        toSave.getChildren().put("name", new TypeInstances(new TypeInstance(user.getName())));
        toSave.getChildren().put("description", new TypeInstances(new TypeInstance(user.getDescription())));
        try {
            persistence.saveValues(user.getPath(), toSave);
        } catch (Throwable t) {
            logger.error("Failed to save new user values", t);
        }
        listeners.put(user, valueBaseWatcher.watch(user.getEmailProperty()));
    }

    @Override
    public void elementRemoved(RealUser user) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(user);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
        try {
            persistence.removeValues(user.getPath());
        } catch(Throwable t) {
            logger.error("Failed to delete automation properties", t);
        }
    }
}

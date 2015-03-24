package com.intuso.housemate.realclient.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealUser;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/02/14
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */
public class UserListWatcher implements ListListener<RealUser> {

    private final Multimap<RealUser, ListenerRegistration> listeners = HashMultimap.create();

    private final Log log;
    private final Persistence persistence;
    private final ValueWatcher valueWatcher;

    @Inject
    public UserListWatcher(Log log, Persistence persistence, ValueWatcher valueWatcher) {
        this.log = log;
        this.persistence = persistence;
        this.valueWatcher = valueWatcher;
    }

    @Override
    public void elementAdded(RealUser user) {
        //listeners.put(application, application.getStatusValue().addObjectListener(valueWatcher));
        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.getChildren().put("id", new TypeInstances(new TypeInstance(user.getId())));
        toSave.getChildren().put("name", new TypeInstances(new TypeInstance(user.getName())));
        toSave.getChildren().put("description", new TypeInstances(new TypeInstance(user.getDescription())));
        try {
            persistence.saveValues(user.getPath(), toSave);
        } catch (HousemateException e) {
            log.e("Failed to save new user values", e);
        }
        listeners.put(user, user.getEmailProperty().addObjectListener(valueWatcher));
    }

    @Override
    public void elementRemoved(RealUser user) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(user);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
        try {
            persistence.removeValues(user.getPath());
        } catch(HousemateException e) {
            log.e("Failed to delete automation properties", e);
        }
    }
}

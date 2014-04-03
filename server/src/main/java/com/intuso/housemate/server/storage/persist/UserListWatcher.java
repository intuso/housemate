package com.intuso.housemate.server.storage.persist;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.server.storage.Storage;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/02/14
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */
public class UserListWatcher implements ListListener<User<?>> {

    private final Map<User<?>, ListenerRegistration> listeners = Maps.newHashMap();

    private final Log log;
    private final Storage storage;

    @Inject
    public UserListWatcher(Log log, Storage storage) {
        this.log = log;
        this.storage = storage;
    }

    @Override
    public void elementAdded(User<?> user) {
        //listeners.put(application, application.getStatusValue().addObjectListener(valueWatcher));
        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.put("id", new TypeInstances(new TypeInstance(user.getId())));
        toSave.put("name", new TypeInstances(new TypeInstance(user.getName())));
        toSave.put("description", new TypeInstances(new TypeInstance(user.getDescription())));
        try {
            storage.saveValues(user.getPath(), toSave);
        } catch (HousemateException e) {
            log.e("Failed to save new user values", e);
        }
    }

    @Override
    public void elementRemoved(User<?> user) {
        ListenerRegistration registration = listeners.remove(user);
        if(registration != null)
            registration.removeListener();
        try {
            storage.removeValues(user.getPath());
        } catch(HousemateException e) {
            log.e("Failed to delete automation properties", e);
        }
    }
}

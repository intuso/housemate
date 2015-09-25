package com.intuso.housemate.server.object.real.persist;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealApplicationInstance;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/02/14
 * Time: 19:51
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationInstanceListWatcher implements List.Listener<RealApplicationInstance> {

    private final Map<ApplicationInstance, ListenerRegistration> listeners = Maps.newHashMap();

    private final Log log;
    private final Persistence persistence;
    private final ValueBaseWatcher valueBaseWatcher;

    @Inject
    public ApplicationInstanceListWatcher(Log log, Persistence persistence, ValueBaseWatcher valueBaseWatcher) {
        this.log = log;
        this.persistence = persistence;
        this.valueBaseWatcher = valueBaseWatcher;
    }

    @Override
    public void elementAdded(RealApplicationInstance applicationInstance) {
        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.getChildren().put("id", new TypeInstances(new TypeInstance(applicationInstance.getId())));
        toSave.getChildren().put("name", new TypeInstances(new TypeInstance(applicationInstance.getName())));
        toSave.getChildren().put("description", new TypeInstances(new TypeInstance(applicationInstance.getDescription())));
        try {
            persistence.saveValues(applicationInstance.getPath(), toSave);
        } catch (Throwable t) {
            log.e("Failed to save new application values", t);
        }
        listeners.put(applicationInstance, valueBaseWatcher.watch(applicationInstance.getStatusValue()));
    }

    @Override
    public void elementRemoved(RealApplicationInstance applicationInstance) {
        ListenerRegistration registration = listeners.remove(applicationInstance);
        if(registration != null)
            registration.removeListener();
        try {
            persistence.removeValues(applicationInstance.getPath());
        } catch(Throwable t) {
            log.e("Failed to delete automation properties", t);
        }
    }
}

package com.intuso.housemate.server.object.real.persist;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealApplicationInstance;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.utilities.listener.ListenerRegistration;
import org.slf4j.Logger;

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

    private final Logger logger;
    private final Persistence persistence;
    private final ValueBaseWatcher valueBaseWatcher;

    @Inject
    public ApplicationInstanceListWatcher(Logger logger, Persistence persistence, ValueBaseWatcher valueBaseWatcher) {
        this.logger = logger;
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
            logger.error("Failed to save new application values", t);
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
            logger.error("Failed to delete automation properties", t);
        }
    }
}

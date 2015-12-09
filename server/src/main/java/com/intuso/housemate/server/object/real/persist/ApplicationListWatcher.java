package com.intuso.housemate.server.object.real.persist;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealApplication;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.utilities.listener.ListenerRegistration;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/02/14
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationListWatcher implements List.Listener<RealApplication> {

    private final Map<Application, ListenerRegistration> listeners = Maps.newHashMap();

    private final Logger logger;
    private final Persistence persistence;
    private final ValueBaseWatcher valueBaseWatcher;
    private final ApplicationInstanceListWatcher instanceListWatcher;

    @Inject
    public ApplicationListWatcher(Logger logger, Persistence persistence, ValueBaseWatcher valueBaseWatcher, ApplicationInstanceListWatcher instanceListWatcher) {
        this.logger = logger;
        this.persistence = persistence;
        this.valueBaseWatcher = valueBaseWatcher;
        this.instanceListWatcher = instanceListWatcher;
    }

    @Override
    public void elementAdded(RealApplication application) {

        // don't save the internal server application
        // todo
//        if(application.getId().equals(Server.INTERNAL_APPLICATION.getApplicationId()))
//            return;

        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.getChildren().put("id", new TypeInstances(new TypeInstance(application.getId())));
        toSave.getChildren().put("name", new TypeInstances(new TypeInstance(application.getName())));
        toSave.getChildren().put("description", new TypeInstances(new TypeInstance(application.getDescription())));
        try {
            persistence.saveValues(application.getPath(), toSave);
        } catch (Throwable t) {
            logger.error("Failed to save new application values", t);
        }
        listeners.put(application, valueBaseWatcher.watch(application.getStatusValue()));
        listeners.put(application, application.getApplicationInstances().addObjectListener(instanceListWatcher, true));
    }

    @Override
    public void elementRemoved(RealApplication application) {
        ListenerRegistration registration = listeners.remove(application);
        if(registration != null)
            registration.removeListener();
        try {
            persistence.removeValues(application.getPath());
        } catch(Throwable t) {
            logger.error("Failed to delete automation properties", t);
        }
    }
}

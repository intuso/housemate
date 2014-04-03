package com.intuso.housemate.server.storage.persist;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.object.application.Application;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.server.Server;
import com.intuso.housemate.server.storage.DetailsNotFoundException;
import com.intuso.housemate.server.storage.Storage;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/02/14
 * Time: 19:41
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationListWatcher implements ListListener<Application<?, ?, ?, ?, ?>> {

    private final Map<Application<?, ?, ?, ?, ?>, ListenerRegistration> listeners = Maps.newHashMap();

    private final Log log;
    private final Storage storage;
    private final ValueWatcher valueWatcher;
    private final ApplicationInstanceListWatcher instanceListWatcher;

    @Inject
    public ApplicationListWatcher(Log log, Storage storage, ValueWatcher valueWatcher, ApplicationInstanceListWatcher instanceListWatcher) {
        this.log = log;
        this.storage = storage;
        this.valueWatcher = valueWatcher;
        this.instanceListWatcher = instanceListWatcher;
    }

    @Override
    public void elementAdded(Application<?, ?, ?, ?, ?> application) {

        // don't save the internal server application
        if(application.getId().equals(Server.INTERNAL_APPLICATION.getApplicationId()))
            return;

        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.put("id", new TypeInstances(new TypeInstance(application.getId())));
        toSave.put("name", new TypeInstances(new TypeInstance(application.getName())));
        toSave.put("description", new TypeInstances(new TypeInstance(application.getDescription())));
        try {
            storage.saveValues(application.getPath(), toSave);
        } catch (HousemateException e) {
            log.e("Failed to save new application values", e);
        }
        listeners.put(application, application.getStatusValue().addObjectListener(valueWatcher));
        listeners.put(application, application.getApplicationInstances().addObjectListener(instanceListWatcher, true));
        try {
            TypeInstances instances = storage.getTypeInstances(application.getStatusValue().getPath());
            if(instances.getFirstValue() != null) {
                ApplicationStatus applicationStatus = ApplicationStatus.valueOf(instances.getFirstValue());
                if(applicationStatus == ApplicationStatus.AllowInstances)
                    application.getAllowCommand().perform(new TypeInstanceMap(),
                        new CommandPerformListener(log, "Allow all application instances \"" + application.getId() + "\""));
                else if(applicationStatus == ApplicationStatus.SomeInstances)
                    application.getSomeCommand().perform(new TypeInstanceMap(),
                            new CommandPerformListener(log, "Allow some application instances \"" + application.getId() + "\""));
                else if(applicationStatus == ApplicationStatus.RejectInstances)
                    application.getRejectCommand().perform(new TypeInstanceMap(),
                            new CommandPerformListener(log, "Reject all application instances \"" + application.getId() + "\""));
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for whether the application was previously accepted" + Arrays.toString(application.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to check value for whether the application was previously accepted", e);
        }
    }

    @Override
    public void elementRemoved(Application<?, ?, ?, ?, ?> application) {
        ListenerRegistration registration = listeners.remove(application);
        if(registration != null)
            registration.removeListener();
        try {
            storage.removeValues(application.getPath());
        } catch(HousemateException e) {
            log.e("Failed to delete automation properties", e);
        }
    }
}

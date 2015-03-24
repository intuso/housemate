package com.intuso.housemate.realclient.persist;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealApplication;
import com.intuso.housemate.persistence.api.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.Persistence;
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
public class ApplicationListWatcher implements ListListener<RealApplication> {

    private final Map<RealApplication, ListenerRegistration> listeners = Maps.newHashMap();

    private final Log log;
    private final Persistence persistence;
    private final ValueWatcher valueWatcher;
    private final ApplicationInstanceListWatcher instanceListWatcher;

    @Inject
    public ApplicationListWatcher(Log log, Persistence persistence, ValueWatcher valueWatcher, ApplicationInstanceListWatcher instanceListWatcher) {
        this.log = log;
        this.persistence = persistence;
        this.valueWatcher = valueWatcher;
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
        } catch (HousemateException e) {
            log.e("Failed to save new application values", e);
        }
        listeners.put(application, application.getStatusValue().addObjectListener(valueWatcher));
        listeners.put(application, application.getApplicationInstances().addObjectListener(instanceListWatcher, true));
        try {
            TypeInstances instances = persistence.getTypeInstances(application.getStatusValue().getPath());
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
    public void elementRemoved(RealApplication application) {
        ListenerRegistration registration = listeners.remove(application);
        if(registration != null)
            registration.removeListener();
        try {
            persistence.removeValues(application.getPath());
        } catch(HousemateException e) {
            log.e("Failed to delete automation properties", e);
        }
    }
}

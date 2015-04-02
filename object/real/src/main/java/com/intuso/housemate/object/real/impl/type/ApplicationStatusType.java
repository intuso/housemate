package com.intuso.housemate.object.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/02/14
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationStatusType extends EnumChoiceType<ApplicationStatus> {

    @Inject
    protected ApplicationStatusType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, "application-status", "Application Status", "Application Status", 1, 1,
                ApplicationStatus.class, new ApplicationStatus[] {ApplicationStatus.Unregistered, ApplicationStatus.AllowInstances, ApplicationStatus.SomeInstances, ApplicationStatus.RejectInstances});
    }
}

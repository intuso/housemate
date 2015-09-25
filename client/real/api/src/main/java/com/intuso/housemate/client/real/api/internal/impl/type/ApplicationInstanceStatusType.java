package com.intuso.housemate.client.real.api.internal.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/02/14
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationInstanceStatusType extends EnumChoiceType<ApplicationInstance.Status> {

    @Inject
    protected ApplicationInstanceStatusType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, "application-instance-status", "Application Instance Status", "Application Instance Status", 1, 1,
                ApplicationInstance.Status.class, ApplicationInstance.Status.Allowed, ApplicationInstance.Status.Rejected);
    }
}

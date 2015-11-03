package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/02/14
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationStatusType extends EnumChoiceType<Application.Status> {

    @Inject
    protected ApplicationStatusType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, "application-status", "Application Status", "Application Status", 1, 1,
                Application.Status.class, Application.Status.Unregistered, Application.Status.AllowInstances, Application.Status.SomeInstances, Application.Status.RejectInstances);
    }
}

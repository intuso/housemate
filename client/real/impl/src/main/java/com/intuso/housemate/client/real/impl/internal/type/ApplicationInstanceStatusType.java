package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/02/14
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationInstanceStatusType extends EnumChoiceType<ApplicationInstance.Status> {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationInstanceStatusType.class);

    @Inject
    protected ApplicationInstanceStatusType(ListenersFactory listenersFactory) {
        super(logger, listenersFactory, "application-instance-status", "Application Instance Status", "Application Instance Status", 1, 1,
                ApplicationInstance.Status.class, ApplicationInstance.Status.Allowed, ApplicationInstance.Status.Rejected);
    }
}

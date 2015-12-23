package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.ApplicationInstance;

/**
 * Created by tomc on 15/09/15.
 */
public interface RequiresAccess {

    Application.Status getApplicationStatus();
    ApplicationInstance.Status getApplicationInstanceStatus();

    /**
     * Logs in to the server
     * @param applicationDetails
     *
     */
    void register(ApplicationDetails applicationDetails);

    /**
     * Logs out of the server
     */
    void unregister();

    interface Listener<REQUIRES_ACCESS extends RequiresAccess> extends com.intuso.utilities.listener.Listener {

        void applicationStatusChanged(REQUIRES_ACCESS requiresAccess, Application.Status applicationStatus);

        void applicationInstanceStatusChanged(REQUIRES_ACCESS requiresAccess, ApplicationInstance.Status applicationInstanceStatus);

        void newApplicationInstance(REQUIRES_ACCESS requiresAccess, String instanceId);
    }
}

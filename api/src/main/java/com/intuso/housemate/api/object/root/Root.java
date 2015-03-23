package com.intuso.housemate.api.object.root;

import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.BaseHousemateObject;

/**
 * @param <ROOT> the type of the root
 */
public interface Root<ROOT extends Root<ROOT>>
        extends BaseHousemateObject<RootListener<? super ROOT>>,
            Receiver<Message.Payload>,
            Sender {

    public final static String SERVER_INSTANCE_ID_TYPE = "server-instance-id";
    public final static String APPLICATION_INSTANCE_ID_TYPE = "application-instance-id";
    public final static String SERVER_CONNECTION_STATUS_TYPE = "server-connection-status";
    public final static String APPLICATION_STATUS_TYPE = "application-status";
    public final static String APPLICATION_INSTANCE_STATUS_TYPE = "application-instance-status";
    public final static String APPLICATION_REGISTRATION_TYPE = "application-registration";
    public final static String APPLICATION_UNREGISTRATION_TYPE = "application-unregistration";
    public final static String CONNECTION_LOST_TYPE = "connection-lost";

    public ApplicationStatus getApplicationStatus();
    public ApplicationInstanceStatus getApplicationInstanceStatus();

    /**
     * Logs in to the server
     * @param applicationDetails
     *
     */
    public void register(ApplicationDetails applicationDetails);

    /**
     * Logs out of the server
     */
    public void unregister();
}

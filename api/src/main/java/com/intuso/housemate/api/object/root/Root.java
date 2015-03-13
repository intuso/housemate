package com.intuso.housemate.api.object.root;

import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * @param <ROOT> the type of the root
 */
public interface Root<ROOT extends Root<?>>
        extends BaseHousemateObject<RootListener<? super ROOT>>, Receiver<Message.Payload>, Sender {

    public final static String SERVER_INSTANCE_ID_TYPE = "server-instance-id";
    public final static String APPLICATION_INSTANCE_ID_TYPE = "application-instance-id";
    public final static String SERVER_CONNECTION_STATUS_TYPE = "server-connection-status";
    public final static String APPLICATION_STATUS_TYPE = "application-status";
    public final static String APPLICATION_INSTANCE_STATUS_TYPE = "application-instance-status";
    public final static String APPLICATION_REGISTRATION_TYPE = "application-registration";
    public final static String APPLICATION_UNREGISTRATION_TYPE = "application-unregistration";
    public final static String CONNECTION_LOST_TYPE = "connection-lost";

    public final static String APPLICATIONS_ID = "applications";
    public final static String USERS_ID = "users";
    public final static String HARDWARES_ID = "hardwares";
    public final static String TYPES_ID = "types";
    public final static String DEVICES_ID = "devices";
    public final static String AUTOMATIONS_ID = "automations";
    public final static String ADD_USER_ID = "add-user";
    public final static String ADD_DEVICE_ID = "add-device";
    public final static String ADD_AUTOMATION_ID = "add-automation";

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

    /**
     * Add a listener for lifecycle updates about an object
     * @param path the path of the object
     * @param listener the listener
     * @return the listener registration
     */
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener);

    /**
     * Gets an object attached to this root
     * @param path the path of the object to get
     * @return the object at that path, or null if there isn't one
     */
    public HousemateObject<?, ?, ?, ?> getObject(String[] path);
}

package com.intuso.housemate.api.object.root;

import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Sender;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * @param <ROOT> the type of the root
 * @param <LISTENER> the type of the root's listener
 */
public interface Root<
            ROOT extends Root,
            LISTENER extends RootListener<? super ROOT>>
        extends BaseObject<LISTENER>, Receiver<Message.Payload>, Sender {

    public final static String STATUS_TYPE = "status";
    public final static String CONNECTION_REQUEST_TYPE = "connection-request";
    public final static String CONNECTION_RESPONSE_TYPE = "connection-response";
    public final static String DISCONNECT_TYPE = "disconnect";
    public final static String CONNECTION_LOST_TYPE = "connection-lost";

    public final static String USERS_ID = "users";
    public final static String TYPES_ID = "types";
    public final static String DEVICES_ID = "devices";
    public final static String AUTOMATIONS_ID = "automations";
    public final static String ADD_USER_ID = "add-user";
    public final static String ADD_DEVICE_ID = "add-device";
    public final static String ADD_AUTOMATION_ID = "add-automation";

    /**
     * Gets the current connection status
     * @return the current connection status
     */
    public ConnectionStatus getStatus();

    /**
     * Gets the connection id
     * @return the connection id
     */
    public String getConnectionId();

    /**
     * Logs in to the broker
     * @param method the method used to authenticate with
     */
    public void login(AuthenticationMethod method);

    /**
     * Logs out of the broker
     */
    public void logout();

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
    public HousemateObject<?, ?, ?, ?, ?> getObject(String[] path);

}

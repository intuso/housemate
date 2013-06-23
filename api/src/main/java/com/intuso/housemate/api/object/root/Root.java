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
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 21:49
 * To change this template use File | Settings | File Templates.
 */
public interface Root<R extends Root, L extends RootListener<? super R>>
        extends BaseObject<L>, Receiver<Message.Payload>, Sender {

    public final static String STATUS = "status";
    public final static String CONNECTION_REQUEST = "connection-request";
    public final static String CONNECTION_RESPONSE = "connection-response";
    public final static String DISCONNECT = "disconnect";
    public final static String CONNECTION_LOST = "connection-lost";

    public final static String USERS = "users";
    public final static String TYPES = "types";
    public final static String DEVICES = "devices";
    public final static String AUTOMATIONS = "automations";
    public final static String ADD_USER = "add-user";
    public final static String ADD_DEVICE = "add-device";
    public final static String ADD_AUTOMATION = "add-automation";

    public ConnectionStatus getStatus();
    public String getConnectionId();
    public void login(AuthenticationMethod method);
    public void logout();
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener);
    public HousemateObject<?, ?, ?, ?, ?> getWrapper(String[] path);

}

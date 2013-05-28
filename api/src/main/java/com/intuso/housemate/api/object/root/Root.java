package com.intuso.housemate.api.object.root;

import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.AuthenticationResponseHandler;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Sender;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.connection.ClientWrappable;
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

    public final static String AUTHENTICATION_REQUEST = "authentication-request";
    public final static String AUTHENTICATION_RESPONSE = "authentication-response";
    public final static String DISCONNECT = "disconnect";
    public final static String USERS = "users";
    public final static String TYPES = "types";
    public final static String DEVICES = "devices";
    public final static String RULES = "rules";
    public final static String ADD_USER = "add-user";
    public final static String ADD_DEVICE = "add-device";
    public final static String ADD_RULE = "add-rule";

    public void connect(AuthenticationMethod method, AuthenticationResponseHandler responseHandler);
    public void disconnect();
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener);
    public HousemateObject<?, ?, ?, ?, ?> getWrapper(String[] path);

    /**
     * Created with IntelliJ IDEA.
     * User: ravnroot
     * Date: 05/03/13
     * Time: 21:56
     * To change this template use File | Settings | File Templates.
     */
    final class AuthenticationRequest implements Message.Payload {

        private ClientWrappable.Type type;
        private AuthenticationMethod method;

        private AuthenticationRequest() {}

        public AuthenticationRequest(ClientWrappable.Type type, AuthenticationMethod method) {
            this.type = type;
            this.method = method;
        }

        public ClientWrappable.Type getType() {
            return type;
        }

        public AuthenticationMethod getMethod() {
            return method;
        }
    }

    /**
     * Created with IntelliJ IDEA.
     * User: ravnroot
     * Date: 22/03/13
     * Time: 09:08
     * To change this template use File | Settings | File Templates.
     */
    final class AuthenticationResponse implements Message.Payload {

        private String connectionId;
        private String userId;
        private String problem;

        private AuthenticationResponse() {}

        public AuthenticationResponse(String connectionId, String userId) {
            this.connectionId = connectionId;
            this.userId = userId;
            this.problem = null;
        }

        public AuthenticationResponse(String problem) {
            this.connectionId = null;
            this.userId = null;
            this.problem = problem;
        }

        public String getConnectionId() {
            return connectionId;
        }

        public String getUserId() {
            return userId;
        }

        public String getProblem() {
            return problem;
        }
    }
}

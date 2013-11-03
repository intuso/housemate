package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.ConnectionType;

/**
 *
 * Message payload for an authentication request
 *
 * @see com.intuso.housemate.api.object.root.Root#login(com.intuso.housemate.api.authentication.AuthenticationMethod)
 */
public final class AuthenticationRequest implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private ConnectionType type;
    private AuthenticationMethod method;

    private AuthenticationRequest() {}

    /**
     * @param type the type of the connection. NB this affects what data you can/can't receive. For user interface
     *             applications you should use {@link com.intuso.housemate.api.comms.ConnectionType#Proxy}
     * @param method the method used for authentication
     */
    public AuthenticationRequest(ConnectionType type, AuthenticationMethod method) {
        this.type = type;
        this.method = method;
    }

    /**
     * Gets the connection type for the login request
     * @return the connection type for the login request
     */
    public ConnectionType getType() {
        return type;
    }

    /**
     * Gets the method of authentication for this login attempt
     * @return the method of authentication for this login attempt
     */
    public AuthenticationMethod getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return type.name() + " " + method;
    }
}

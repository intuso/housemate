package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.ConnectionType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 05/03/13
 * Time: 21:56
 * To change this template use File | Settings | File Templates.
 */
public final class AuthenticationRequest implements Message.Payload {

    private ConnectionType type;
    private AuthenticationMethod method;

    private AuthenticationRequest() {}

    public AuthenticationRequest(ConnectionType type, AuthenticationMethod method) {
        this.type = type;
        this.method = method;
    }

    public ConnectionType getType() {
        return type;
    }

    public AuthenticationMethod getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return type.name() + " " + method;
    }
}

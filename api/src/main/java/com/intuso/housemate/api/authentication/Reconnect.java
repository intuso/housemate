package com.intuso.housemate.api.authentication;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 31/05/13
 * Time: 18:42
 * To change this template use File | Settings | File Templates.
 */
public class Reconnect extends AuthenticationMethod {

    private String connectionId;

    public Reconnect(String connectionId) {
        super(true);
        this.connectionId = connectionId;
    }

    public String getConnectionId() {
        return connectionId;
    }
}

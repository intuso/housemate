package com.intuso.housemate.api.authentication;

/**
 *
 * Authentication method to reconnect with a connection id
 *
 * @see com.intuso.housemate.api.object.root.Root#login(AuthenticationMethod)
 */
public class Reconnect implements AuthenticationMethod {

    private String connectionId;

    private Reconnect() {}

    /**
     * @param connectionId the previous connection id
     */
    public Reconnect(String connectionId) {
        this.connectionId = connectionId;
    }

    /**
     * Gets the previous connection id
     * @return the previous connection id
     */
    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public String toString() {
        return "reconnect connId=" + connectionId;
    }
}

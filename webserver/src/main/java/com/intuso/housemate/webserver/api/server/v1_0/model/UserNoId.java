package com.intuso.housemate.webserver.api.server.v1_0.model;

/**
 * Created by tomc on 21/01/17.
 */
public class UserNoId {

    private String email;
    private String serverAddress;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}

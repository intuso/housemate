package com.intuso.housemate.webserver.api.server.v1_0.model;

/**
 * Created by tomc on 21/01/17.
 */
public class ClientNoIdSecret {

    private String userId;
    private String name;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

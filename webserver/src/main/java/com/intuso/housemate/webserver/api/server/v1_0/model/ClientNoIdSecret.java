package com.intuso.housemate.webserver.api.server.v1_0.model;

/**
 * Created by tomc on 21/01/17.
 */
public class ClientNoIdSecret {

    private String ownerId;
    private String name;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

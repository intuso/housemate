package com.intuso.housemate.webserver.api.server.v1_0.model;

/**
 * Created by tomc on 21/01/17.
 */
public class Client {

    private String ownerId;
    private String id;
    private String secret;
    private String name;

    public Client() {}

    public Client(String ownerId, String id, String secret, String name) {
        this.ownerId = ownerId;
        this.id = id;
        this.secret = secret;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public static Client from(com.intuso.utilities.webserver.oauth.model.Client client) {
        return client == null ? null : new Client(client.getOwnerId(),
                client.getId(),
                client.getSecret(),
                client.getName());
    }
}

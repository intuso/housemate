package com.intuso.housemate.webserver.api.server.v1_0.model;

/**
 * Created by tomc on 21/01/17.
 */
public class User {

    private String id;
    private String email;
    private String serverAddress;

    public User() {}

    public User(String id, String email, String serverAddress) {
        this.id = id;
        this.email = email;
        this.serverAddress = serverAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public static User from(com.intuso.housemate.webserver.database.model.User user) {
        return user == null ? null : new User(user.getId(), user.getEmail(), user.getServerAddress());
    }
}

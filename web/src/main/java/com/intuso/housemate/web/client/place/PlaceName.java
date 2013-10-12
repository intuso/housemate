package com.intuso.housemate.web.client.place;

/**
 */
public enum PlaceName {

    Devices("#devices:"),
    Automations("#automations:"),
    Users("#users:"),
    Account("#account:");

    private String token;

    private PlaceName(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

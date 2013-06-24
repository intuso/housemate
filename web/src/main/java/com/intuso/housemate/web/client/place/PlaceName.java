package com.intuso.housemate.web.client.place;

/**
 */
public enum PlaceName {

    Account("#account:"),
    Home("#home:"),
    User("#user:"),
    Device("#device:"),
    Automation("#automation:"),
    Condition("#condition:"),
    SatisfiedTask("#satisfiedtask:"),
    UnsatisfiedTask("#unsatisfiedtask:");

    private String token;

    private PlaceName(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

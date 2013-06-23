package com.intuso.housemate.web.client.place;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 30/11/12
 * Time: 08:19
 * To change this template use File | Settings | File Templates.
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

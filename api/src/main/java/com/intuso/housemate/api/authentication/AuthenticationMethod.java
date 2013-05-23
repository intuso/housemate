package com.intuso.housemate.api.authentication;

import java.io.Serializable;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 02/05/13
* Time: 18:25
* To change this template use File | Settings | File Templates.
*/
public class AuthenticationMethod implements Serializable {

    private boolean allowReconnect;

    protected AuthenticationMethod() {}

    public AuthenticationMethod(boolean allowReconnect) {
        this.allowReconnect = allowReconnect;
    }

    public boolean isAllowReconnect() {
        return allowReconnect;
    }
}

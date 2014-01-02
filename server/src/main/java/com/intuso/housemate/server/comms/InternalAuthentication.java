package com.intuso.housemate.server.comms;

import com.intuso.housemate.api.authentication.AuthenticationMethod;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 31/10/13
* Time: 09:17
* To change this template use File | Settings | File Templates.
*/
public final class InternalAuthentication implements AuthenticationMethod {

    private static final long serialVersionUID = -1L;

    @Override
    public boolean isClientsAuthenticated() {
        return false;
    }

    @Override
    public String toString() {
        return "internal";
    }
}

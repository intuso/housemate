package com.intuso.housemate.api.authentication;

/**
 *
 * Authentication method to log in with a username and password.
 *
 * @see com.intuso.housemate.api.object.root.Root#login(com.intuso.housemate.api.authentication.AuthenticationMethod)
 */
public class FromRouter implements AuthenticationMethod {

    private static final long serialVersionUID = -1L;

    public FromRouter() {}

    @Override
    public boolean isClientsAuthenticated() {
        return false;
    }

    @Override
    public String toString() {
        return "from router";
    }
}

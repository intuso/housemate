package com.intuso.housemate.api.authentication;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 02/05/13
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class UsernamePassword extends AuthenticationMethod {

    private String username;
    private String password;
    private boolean createSession;

    private UsernamePassword() {}

    public UsernamePassword(boolean allowReconnect, String username, String password, boolean createSession) {
        super(allowReconnect);
        this.username = username;
        this.password = password;
        this.createSession = createSession;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isCreateSession() {
        return createSession;
    }
}

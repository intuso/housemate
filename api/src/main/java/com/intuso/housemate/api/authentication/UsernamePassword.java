package com.intuso.housemate.api.authentication;

/**
 *
 * Authentication method to log in with a username and password.
 *
 * @see com.intuso.housemate.api.object.root.Root#login(AuthenticationMethod)
 */
public class UsernamePassword implements AuthenticationMethod {

    private static final long serialVersionUID = -1L;

    private String username;
    private String password;
    private boolean createSession;
    private boolean childrenAuthenticated;

    private UsernamePassword() {}

    /**
     * @param username the username to login with
     * @param password the password to login with
     * @param createSession true to create a session id that can be used at next login. Useful in user interfaces where
     *                      you don't want the user to be prompted for their credentials each time
     */
    public UsernamePassword(String username, String password, boolean createSession) {
        this(username, password, createSession, false);
    }

    /**
     * @param username the username to login with
     * @param password the password to login with
     * @param createSession true to create a session id that can be used at next login. Useful in user interfaces where
     *                      you don't want the user to be prompted for their credentials each time
     */
    public UsernamePassword(String username, String password, boolean createSession, boolean childrenAuthenticated) {
        this.username = username;
        this.password = password;
        this.createSession = createSession;
        this.childrenAuthenticated = childrenAuthenticated;
    }

    /**
     * Gets the username to login with
     * @return the username to login with
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password to login with
     * @return the password to login with
     */
    public String getPassword() {
        return password;
    }

    /**
     * Checks whether a session should be created for this login attempt
     * @return true if a session should be created for this login attempt
     */
    public boolean isCreateSession() {
        return createSession;
    }

    @Override
    public boolean isClientsAuthenticated() {
        return childrenAuthenticated;
    }

    @Override
    public String toString() {
        return "username/password=" + username + "/guessme";
    }
}

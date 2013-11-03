package com.intuso.housemate.api.authentication;

/**
 *
 * Authentication method to log in with a session id.
 *
 * @see com.intuso.housemate.api.object.root.Root#login(AuthenticationMethod)
 */
public class Session implements AuthenticationMethod {

    private static final long serialVersionUID = -1L;

    private String sessionId;
    private boolean childrenAuthenticated;

    private Session() {}

    /**
     * @param sessionId the saved session id
     */
    public Session(String sessionId) {
        this(sessionId, false);
    }

    /**
     * @param sessionId the saved session id
     */
    public Session(String sessionId, boolean childrenAuthenticated) {
        this.sessionId = sessionId;
        this.childrenAuthenticated = childrenAuthenticated;
    }

    /**
     * Gets the saved session id
     * @return the saved session id
     */
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public boolean isClientsAuthenticated() {
        return childrenAuthenticated;
    }

    @Override
    public String toString() {
        return "session=" + sessionId;
    }
}

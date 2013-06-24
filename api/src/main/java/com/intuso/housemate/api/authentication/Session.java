package com.intuso.housemate.api.authentication;

/**
 *
 * Authentication method to log in with a session id.
 *
 * @see com.intuso.housemate.api.object.root.Root#login(AuthenticationMethod)
 */
public class Session implements AuthenticationMethod {

    private String sessionId;

    private Session() {}

    /**
     * @param sessionId the saved session id
     */
    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Gets the saved session id
     * @return the saved session id
     */
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "session=" + sessionId;
    }
}

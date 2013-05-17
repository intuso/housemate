package com.intuso.housemate.core.authentication;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 02/05/13
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class Session extends AuthenticationMethod {

    private String sessionId;

    private Session() {}

    public Session(boolean allowReconnect, String sessionId) {
        super(allowReconnect);
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}

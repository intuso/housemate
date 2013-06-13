package com.intuso.housemate.api.comms.message;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 12/06/13
* Time: 20:07
* To change this template use File | Settings | File Templates.
*/
public class ReconnectResponse extends AuthenticationResponse {
    public ReconnectResponse() {}

    @Override
    public String toString() {
        return "reconnected";
    }
}

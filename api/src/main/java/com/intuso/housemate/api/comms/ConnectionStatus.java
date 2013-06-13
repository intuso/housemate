package com.intuso.housemate.api.comms;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 12/06/13
* Time: 20:04
* To change this template use File | Settings | File Templates.
*/
public enum ConnectionStatus implements Message.Payload {
    Disconnected,
    Connecting,
    Unauthenticated,
    Authenticating,
    Authenticated,
    AuthenticationFailed
}

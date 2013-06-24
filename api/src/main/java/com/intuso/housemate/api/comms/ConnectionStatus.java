package com.intuso.housemate.api.comms;

/**
 *
 * Enumeration of all the possible states during a (dis)connect process
*/
public enum ConnectionStatus implements Message.Payload {
    Disconnected,
    Connecting,
    Unauthenticated,
    Authenticating,
    Authenticated,
    AuthenticationFailed
}

package com.intuso.housemate.api.comms.message;

/**
 *
 * Special authentication response for successful reconnect attempts
*/
public class ReconnectResponse extends AuthenticationResponse {
    public ReconnectResponse() {}

    @Override
    public String toString() {
        return "reconnected";
    }
}

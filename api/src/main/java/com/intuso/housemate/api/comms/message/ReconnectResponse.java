package com.intuso.housemate.api.comms.message;

/**
 *
 * Special authentication response for successful reconnect attempts
*/
public class ReconnectResponse extends AuthenticationResponse {

    private static final long serialVersionUID = -1L;

    public ReconnectResponse() {}

    @Override
    public String toString() {
        return "reconnected";
    }
}

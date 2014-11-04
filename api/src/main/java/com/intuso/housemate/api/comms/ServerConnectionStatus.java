package com.intuso.housemate.api.comms;

/**
 * Enum of possible connection statuses for a router
 */
public enum ServerConnectionStatus implements Message.Payload {
    DisconnectedPermanently,
    DisconnectedTemporarily,
    Connecting,
    ConnectedToRouter,
    ConnectedToServer;

    @Override
    public void ensureSerialisable() {}
}

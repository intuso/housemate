package com.intuso.housemate.comms.api.internal.access;

import com.intuso.housemate.comms.api.internal.Message;

/**
 * Enum of possible connection statuses for a router
 */
public enum ConnectionStatus implements Message.Payload {
    DisconnectedPermanently,
    DisconnectedTemporarily,
    Connecting,
    ConnectedToRouter,
    ConnectedToServer;

    @Override
    public void ensureSerialisable() {}
}

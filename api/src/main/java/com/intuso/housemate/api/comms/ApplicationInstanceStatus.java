package com.intuso.housemate.api.comms;

/**
 *
 * Enumeration of all the possible states during a (dis)connect process
*/
public enum ApplicationInstanceStatus implements Message.Payload {
    Unregistered,
    Registering,
    Allowed,
    Pending,
    Rejected,
    Expired;

    @Override
    public void ensureSerialisable() {}
}

package com.intuso.housemate.api.comms;

/**
 *
 * Enumeration of all the possible states during a (dis)connect process
*/
public enum ApplicationInstanceStatus implements Message.Payload {
    Unregistered,
    Allowed,
    Pending,
    Rejected,
    Expired;

    @Override
    public void ensureSerialisable() {}
}

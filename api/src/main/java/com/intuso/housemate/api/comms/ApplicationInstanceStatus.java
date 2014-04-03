package com.intuso.housemate.api.comms;

/**
 *
 * Enumeration of all the possible states during a (dis)connect process
*/
public enum ApplicationInstanceStatus {
    Unregistered,
    Allowed,
    Pending,
    Rejected,
    Expired
}

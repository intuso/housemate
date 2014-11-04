package com.intuso.housemate.api.comms;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 11/02/14
 * Time: 08:59
 * To change this template use File | Settings | File Templates.
 */
public enum ApplicationStatus implements Message.Payload {
    Unregistered,
    AllowInstances,
    SomeInstances,
    RejectInstances,
    Expired;

    @Override
    public void ensureSerialisable() {}
}

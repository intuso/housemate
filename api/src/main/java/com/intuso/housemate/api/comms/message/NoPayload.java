package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.comms.Message;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 31/03/12
 * Time: 19:11
 * To change this template use File | Settings | File Templates.
 */
public class NoPayload implements Message.Payload {
    public final static NoPayload VALUE = new NoPayload();

    private NoPayload() {}

    @Override
    public String toString() {
        return "";
    }
}

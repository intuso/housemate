package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.comms.Message;

/**
 *
 * Message payload for messages which have no payload
 */
public class NoPayload implements Message.Payload {
    public final static NoPayload VALUE = new NoPayload();

    private NoPayload() {}

    @Override
    public String toString() {
        return "";
    }
}

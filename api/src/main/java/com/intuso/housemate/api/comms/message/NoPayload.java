package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.comms.Message;

/**
 *
 * Message payload for messages which have no payload
 */
public class NoPayload implements Message.Payload {

    private static final long serialVersionUID = -1L;

    public final static NoPayload INSTANCE = new NoPayload();

    private NoPayload() {}

    @Override
    public String toString() {
        return "";
    }

    @Override
    public void ensureSerialisable() {}
}

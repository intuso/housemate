package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.comms.Message;

/**
 *
 * Message payload for strings
 */
public class StringPayload implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private String value;

    public StringPayload() {}

    /**
     * @param value the payload to send
     */
    public StringPayload(String value) {
        this.value = value;
    }

    /**
     * Gets the string payload
     * @return the string payload
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public void ensureSerialisable() {}
}

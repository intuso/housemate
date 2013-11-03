package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.comms.Message;

/**
 *
 * Message payload for strings
 */
public class StringPayload implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private String value;

    private StringPayload() {}

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

    @Override
    public String toString() {
        return value;
    }
}

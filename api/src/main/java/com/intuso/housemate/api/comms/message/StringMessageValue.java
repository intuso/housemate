package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.comms.Message;

/**
 *
 * Message payload for strings
 */
public class StringMessageValue implements Message.Payload {

    private String value;

    private StringMessageValue() {}

    /**
     * @param value the payload to send
     */
    public StringMessageValue(String value) {
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

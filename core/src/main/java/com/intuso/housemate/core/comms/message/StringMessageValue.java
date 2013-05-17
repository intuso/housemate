package com.intuso.housemate.core.comms.message;

import com.intuso.housemate.core.comms.Message;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 31/03/12
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
public class StringMessageValue implements Message.Payload {
    String value;

    private StringMessageValue() {}

    public StringMessageValue(String value) {
        this.value = value;
    }

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
}

package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.comms.Message;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 31/03/12
 * Time: 19:11
 * To change this template use File | Settings | File Templates.
 */
public class EmptyMessageValue implements Message.Payload {
    public final static EmptyMessageValue VALUE = new EmptyMessageValue();

    private EmptyMessageValue() {}

    @Override
    public String toString() {
        return "";
    }
}

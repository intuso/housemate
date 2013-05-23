package com.intuso.housemate.api.comms;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 00:01
 * To change this template use File | Settings | File Templates.
 */
public interface Sender {
    void sendMessage(Message<?> message);
}

package com.intuso.housemate.api.comms;

/**
 *
 * Sender of messages to the broker
 */
public interface Sender {

    /**
     * Sends a message to the broker
     * @param message the message to send
     */
    void sendMessage(Message<?> message);
}

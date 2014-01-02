package com.intuso.housemate.api.comms;

/**
 *
 * Sender of messages to the server
 */
public interface Sender {

    /**
     * Sends a message to the server
     * @param message the message to send
     */
    void sendMessage(Message<?> message);
}

package com.intuso.housemate.client.messaging.api.internal;

import org.slf4j.Logger;

import java.io.Serializable;

/**
 * Created by tomc on 01/12/16.
 */
public interface Sender {

    void close();
    void send(Serializable object, boolean persistent);

    interface Factory {
        Sender create(Logger logger, String name);
    }
}

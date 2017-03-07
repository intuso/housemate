package com.intuso.housemate.client.messaging.api.internal;

import org.slf4j.Logger;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by tomc on 01/12/16.
 */
public interface Receiver<T extends Serializable> {

    void close();
    T getPersistedMessage();
    Iterator<T> getPersistedMessages();
    void listen(Listener<T> listener);

    interface Listener<T extends Serializable> {
        void onMessage(T t, boolean wasPersisted);
    }

    interface Factory {
        <T extends Serializable> Receiver<T> create(Logger logger, String name, Class<T> tClass);
    }
}

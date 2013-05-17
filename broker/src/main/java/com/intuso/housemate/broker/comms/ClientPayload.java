package com.intuso.housemate.broker.comms;

import com.intuso.housemate.broker.client.RemoteClient;
import com.intuso.housemate.core.comms.Message;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/04/13
 * Time: 14:00
 * To change this template use File | Settings | File Templates.
 */
public class ClientPayload<T extends Message.Payload> implements Message.Payload {

    private final RemoteClient client;
    private final T original;

    public ClientPayload(RemoteClient client, T original) {
        this.client = client;
        this.original = original;
    }

    public RemoteClient getClient() {
        return client;
    }

    public T getOriginal() {
        return original;
    }
}

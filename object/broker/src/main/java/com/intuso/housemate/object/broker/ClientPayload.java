package com.intuso.housemate.object.broker;

import com.intuso.housemate.api.comms.Message;

/**
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

    @Override
    public String toString() {
        return "from: " + client.getConnectionId() + ", message: " + original.toString();
    }
}

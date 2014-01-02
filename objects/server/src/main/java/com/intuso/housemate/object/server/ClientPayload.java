package com.intuso.housemate.object.server;

import com.intuso.housemate.api.comms.Message;

/**
 * Message payload the wraps the normal client payload and provides extra information about the client that sent it
 *
 * @param <PAYLOAD> The type of the payload sent by the client
 */
public class ClientPayload<PAYLOAD extends Message.Payload> implements Message.Payload {

    private final RemoteClient client;
    private final PAYLOAD original;

    /**
     * @param client the client that sent the original message
     * @param original the original payload
     */
    public ClientPayload(RemoteClient client, PAYLOAD original) {
        this.client = client;
        this.original = original;
    }

    /**
     * Gets the client that sent the original message
     * @return the client that sent the original message
     */
    public RemoteClient getClient() {
        return client;
    }

    /**
     * Gets the original payload
     * @return the original payload
     */
    public PAYLOAD getOriginal() {
        return original;
    }

    @Override
    public String toString() {
        return "from: " + client.getConnectionId() + ", message: " + original.toString();
    }
}

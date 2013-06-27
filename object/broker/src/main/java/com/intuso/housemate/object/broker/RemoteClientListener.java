package com.intuso.housemate.object.broker;

import com.intuso.utilities.listener.Listener;

public interface RemoteClientListener extends Listener {

    /**
     * Notifies that a client is now disconnected
     * @param client the client that disconnected
     */
    public void disconnected(RemoteClient client);

    /**
     * Notifies that a client lost their connection
     * @param client the client that lost their connection
     */
    public void connectionLost(RemoteClient client);

    /**
     * Notifies that a client is reconnected
     * @param client the client that reconnected
     */
    public void reconnected(RemoteClient client);
}

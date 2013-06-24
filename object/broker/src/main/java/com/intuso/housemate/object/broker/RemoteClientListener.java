package com.intuso.housemate.object.broker;

import com.intuso.utilities.listener.Listener;

/**
 */
public interface RemoteClientListener extends Listener {
    public void disconnected(RemoteClient client);
    public void connectionLost(RemoteClient client);
    public void reconnected(RemoteClient client);
}

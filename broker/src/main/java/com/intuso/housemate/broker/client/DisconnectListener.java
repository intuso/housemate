package com.intuso.housemate.broker.client;

import com.intuso.listeners.Listener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 14/05/13
 * Time: 18:56
 * To change this template use File | Settings | File Templates.
 */
public interface DisconnectListener extends Listener {
    public void disconnected(RemoteClient client);
}

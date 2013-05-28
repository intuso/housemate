package com.intuso.housemate.object.broker;

import com.intuso.utilities.listener.Listener;

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

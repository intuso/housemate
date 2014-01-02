package com.intuso.housemate.api.comms;

import com.intuso.utilities.listener.Listener;

/**
 *
 * Listener for all changes in a connection status
 */
public interface ConnectionStatusChangeListener extends Listener {

    /**
     * Notifies that a connection status has changed
     * @param status the new connection status
     */
    public void connectionStatusChanged(ConnectionStatus status);

    /**
     * Notifies when the instance of the server has changed
     */
    public void newServerInstance();
}

package com.intuso.housemate.api.object.root;

import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.object.ObjectListener;

/**
 *
 * Listener interface for root objects
 */
public interface RootListener<R extends Root<?>> extends ObjectListener {

    /**
     * Notifies that the connection status of this root object has changed
     * @param root the root object whose connection status has changed
     * @param status the new connection status
     */
    public void connectionStatusChanged(R root, ConnectionStatus status);

    /**
     * Notifies that the instance of the connected server has changed
     * @param root the root whose server instance was changed
     */
    void newServerInstance(R root);
}

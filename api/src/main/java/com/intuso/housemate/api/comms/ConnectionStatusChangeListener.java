package com.intuso.housemate.api.comms;

import com.intuso.utilities.listener.Listener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 12/06/13
 * Time: 20:00
 * To change this template use File | Settings | File Templates.
 */
public interface ConnectionStatusChangeListener extends Listener {
    public void connectionStatusChanged(ConnectionStatus status);
    public void brokerInstanceChanged();
}

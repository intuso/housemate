package com.intuso.housemate.api.comms;

import com.intuso.utilities.listener.Listener;

/**
 *
 * Listener for all changes in a connection status
 */
public interface ConnectionListener extends Listener {

    public void statusChanged(ServerConnectionStatus serverConnectionStatus, ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus);

    public void newApplicationInstance(String instanceId);

    /**
     * Notifies when the instance of the server has changed
     * @param serverId
     */
    public void newServerInstance(String serverId);
}

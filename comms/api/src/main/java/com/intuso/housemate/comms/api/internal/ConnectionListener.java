package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.utilities.listener.Listener;

/**
 *
 * Listener for all changes in a connection status
 */
public interface ConnectionListener extends Listener {

    public void serverConnectionStatusChanged(ServerConnectionStatus serverConnectionStatus);

    public void applicationStatusChanged(Application.Status applicationStatus);

    public void applicationInstanceStatusChanged(ApplicationInstance.Status applicationInstanceStatus);

    public void newApplicationInstance(String instanceId);

    /**
     * Notifies when the instance of the server has changed
     * @param serverId
     */
    public void newServerInstance(String serverId);
}

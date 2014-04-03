package com.intuso.housemate.api.object.root;

import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.object.ObjectListener;

/**
 *
 * Listener interface for root objects
 */
public interface RootListener<ROOT extends Root<?>> extends ObjectListener {

    public void statusChanged(ROOT root, ServerConnectionStatus serverConnectionStatus, ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus);

    public void newApplicationInstance(ROOT root, String instanceId);

    /**
     * Notifies when the instance of the server has changed
     * @param root the root object the listener was attached to
     * @param serverId
     */
    public void newServerInstance(ROOT root, String serverId);
}

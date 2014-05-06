package com.intuso.housemate.object.server;

import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.utilities.listener.Listener;

public interface RemoteClientListener extends Listener {
    public void statusChanged(ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus);
    public void disconnected(RemoteClient client);
}

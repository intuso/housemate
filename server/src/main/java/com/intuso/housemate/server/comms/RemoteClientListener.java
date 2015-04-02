package com.intuso.housemate.server.comms;

import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.utilities.listener.Listener;

public interface RemoteClientListener extends Listener {
    public void statusChanged(ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus);
    public void unregistered(RemoteClient client);
}

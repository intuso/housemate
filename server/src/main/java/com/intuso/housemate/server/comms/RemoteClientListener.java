package com.intuso.housemate.server.comms;

import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.utilities.listener.Listener;

public interface RemoteClientListener extends Listener {
    public void statusChanged(Application.Status applicationStatus, ApplicationInstance.Status applicationInstanceStatus);
    public void unregistered(RemoteClient client);
}

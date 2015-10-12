package com.intuso.housemate.comms.api.bridge.v1_0;

import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;

/**
 * Created by tomc on 02/10/15.
 */
public class ServerConnectionStatusMapper {

    public ServerConnectionStatus map(com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus serverConnectionStatus) {
        if(serverConnectionStatus == null)
            return null;
        return ServerConnectionStatus.valueOf(serverConnectionStatus.name());
    }

    public com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus map(ServerConnectionStatus serverConnectionStatus) {
        if(serverConnectionStatus == null)
            return null;
        return com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus.valueOf(serverConnectionStatus.name());
    }
}

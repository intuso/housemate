package com.intuso.housemate.comms.api.bridge.v1_0;

import com.intuso.housemate.comms.api.internal.access.ConnectionStatus;

/**
 * Created by tomc on 02/10/15.
 */
public class ConnectionStatusMapper {

    public ConnectionStatus map(com.intuso.housemate.comms.v1_0.api.access.ConnectionStatus connectionStatus) {
        if(connectionStatus == null)
            return null;
        return ConnectionStatus.valueOf(connectionStatus.name());
    }

    public com.intuso.housemate.comms.v1_0.api.access.ConnectionStatus map(ConnectionStatus connectionStatus) {
        if(connectionStatus == null)
            return null;
        return com.intuso.housemate.comms.v1_0.api.access.ConnectionStatus.valueOf(connectionStatus.name());
    }
}

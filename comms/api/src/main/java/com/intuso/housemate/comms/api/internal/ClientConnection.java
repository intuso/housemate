package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.access.ConnectionStatus;

/**
 * Created by tomc on 15/09/15.
 */
public interface ClientConnection {

    String SERVER_INSTANCE_ID_TYPE = "server-instance-id";
    String NEXT_CONNECTION_STATUS_TYPE = "next-connection-status";
    String UNKNOWN_CLIENT_ID = "unknown-client-id";

    ConnectionStatus getConnectionStatus();

    void connect();
    void disconnect();

    interface Listener<CLIENT_CONNECTION extends ClientConnection> extends com.intuso.utilities.listener.Listener {

        void serverConnectionStatusChanged(CLIENT_CONNECTION clientConnection, ConnectionStatus connectionStatus);

        /**
         * Notifies when the instance of the server has changed
         * @param clientConnection the object the listener was attached to
         * @param serverId
         */
        void newServerInstance(CLIENT_CONNECTION clientConnection, String serverId);
    }
}

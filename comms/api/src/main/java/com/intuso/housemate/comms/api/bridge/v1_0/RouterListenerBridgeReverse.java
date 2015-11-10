package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.comms.api.internal.access.ConnectionStatus;

/**
 * Created by tomc on 02/10/15.
 */
public class RouterListenerBridgeReverse implements Router.Listener<Router> {

    private final ConnectionStatusMapper connectionStatusMapper;
    private final com.intuso.housemate.comms.v1_0.api.Router router;
    private final com.intuso.housemate.comms.v1_0.api.Router.Listener listener;

    @Inject
    public RouterListenerBridgeReverse(ConnectionStatusMapper connectionStatusMapper,
                                       @Assisted com.intuso.housemate.comms.v1_0.api.Router<?> router,
                                       @Assisted com.intuso.housemate.comms.v1_0.api.Router.Listener listener) {
        this.connectionStatusMapper = connectionStatusMapper;
        this.router = router;
        this.listener = listener;
    }

    @Override
    public void serverConnectionStatusChanged(Router clientConnection, ConnectionStatus connectionStatus) {
        listener.serverConnectionStatusChanged(router, connectionStatusMapper.map(connectionStatus));
    }

    @Override
    public void newServerInstance(Router clientConnection, String serverId) {
        listener.newServerInstance(router, serverId);
    }

    public interface Factory {
        RouterListenerBridgeReverse create(com.intuso.housemate.comms.v1_0.api.Router<?> router, com.intuso.housemate.comms.v1_0.api.Router.Listener listener);
    }
}

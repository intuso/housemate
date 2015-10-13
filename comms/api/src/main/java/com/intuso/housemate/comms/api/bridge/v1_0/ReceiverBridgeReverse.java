package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;

/**
 * Created by tomc on 02/10/15.
 */
public class ReceiverBridgeReverse<ROUTER extends Router<?>> implements Router.Receiver<ROUTER> {

    private final MessageMapper messageMapper;
    private final com.intuso.housemate.comms.v1_0.api.Router router;
    private final com.intuso.housemate.comms.v1_0.api.Router.Receiver receiver;

    @Inject
    public ReceiverBridgeReverse(MessageMapper messageMapper,
                                 @Assisted com.intuso.housemate.comms.v1_0.api.Router router,
                                 @Assisted com.intuso.housemate.comms.v1_0.api.Router.Receiver receiver) {
        this.messageMapper = messageMapper;
        this.router = router;
        this.receiver = receiver;
    }

    @Override
    public void messageReceived(Message message) {
        receiver.messageReceived(messageMapper.map(message));
    }

    @Override
    public void serverConnectionStatusChanged(ROUTER router, ServerConnectionStatus serverConnectionStatus) {
        receiver.serverConnectionStatusChanged(this.router, com.intuso.housemate.comms.v1_0.api.access.ServerConnectionStatus.valueOf(serverConnectionStatus.name()));
    }

    @Override
    public void newServerInstance(ROUTER router, String serverId) {
        receiver.newServerInstance(this.router, serverId);
    }

    public interface Factory {
        ReceiverBridgeReverse create(com.intuso.housemate.comms.v1_0.api.Router router, com.intuso.housemate.comms.v1_0.api.Router.Receiver receiver);
    }
}

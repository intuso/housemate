package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.inject.Inject;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 * Created by tomc on 25/09/15.
 */
public class RouterV1_0Bridge extends Router {

    private final MessageMapper messageMapper;
    private final com.intuso.housemate.comms.api.internal.Router router;

    @Inject
    public RouterV1_0Bridge(Log log, ListenersFactory listenersFactory, PropertyRepository properties, MessageMapper messageMapper, com.intuso.housemate.comms.api.internal.Router router) {
        super(log, listenersFactory, properties);
        this.messageMapper = messageMapper;
        this.router = router;
    }

    @Override
    public void connect() {
        router.connect();
    }

    @Override
    public void disconnect() {
        router.disconnect();
    }

    @Override
    public void sendMessage(Message<?> message) {
        router.sendMessage(messageMapper.map(message));
    }
}

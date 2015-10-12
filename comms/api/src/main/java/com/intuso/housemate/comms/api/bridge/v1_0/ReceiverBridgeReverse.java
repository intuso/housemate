package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.Message;

/**
 * Created by tomc on 02/10/15.
 */
public class ReceiverBridgeReverse<PAYLOAD extends Message.Payload> implements Message.Receiver<PAYLOAD> {

    private final MessageMapper messageMapper;
    private final com.intuso.housemate.comms.v1_0.api.Message.Receiver<com.intuso.housemate.comms.v1_0.api.Message.Payload> receiver;

    @Inject
    public ReceiverBridgeReverse(MessageMapper messageMapper,
                                 @Assisted com.intuso.housemate.comms.v1_0.api.Message.Receiver<com.intuso.housemate.comms.v1_0.api.Message.Payload> receiver) {
        this.messageMapper = messageMapper;
        this.receiver = receiver;
    }

    @Override
    public void messageReceived(Message<PAYLOAD> message) {
        receiver.messageReceived(messageMapper.map(message));
    }

    public interface Factory {
        ReceiverBridgeReverse create(com.intuso.housemate.comms.v1_0.api.Message.Receiver<com.intuso.housemate.comms.v1_0.api.Message.Payload> receiver);
    }
}

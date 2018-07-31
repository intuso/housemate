package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;

/**
 * Created by tomc on 28/11/16.
 */
public interface ServerBaseNode<COMMAND extends Command<?, ?, ?, ?>,
        TYPES extends List<? extends Type<?>, ?>,
        HARDWARES extends List<? extends Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        NODE extends ServerBaseNode<COMMAND, TYPES, HARDWARES, NODE>>
        extends Node<COMMAND, TYPES, HARDWARES, NODE> {
    void init(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory);
    void uninit();
}

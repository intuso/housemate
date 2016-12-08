package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.api.internal.object.*;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public interface ServerBaseNode<COMMAND extends Command<?, ?, ?, ?>,
        TYPES extends List<? extends Type<?>, ?>,
        HARDWARES extends List<? extends Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        NODE extends ServerBaseNode<COMMAND, TYPES, HARDWARES, NODE>>
        extends Node<COMMAND, TYPES, HARDWARES, NODE> {
    void init(String name, Connection connection) throws JMSException;
    void uninit();
}

package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.real.api.internal.*;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public interface RealNodeImplBase<COMMAND extends RealCommand<?, ?, ?>,
        TYPES extends RealList<? extends RealType<?, ?>, ?>,
        HARDWARE extends RealHardware<?, ?, ?, ?, ?, ?>,
        HARDWARES extends RealList<? extends HARDWARE, ?>,
        NODE extends RealNodeImplBase<COMMAND, TYPES, HARDWARE, HARDWARES, NODE>>
        extends RealNode<COMMAND, TYPES, HARDWARE, HARDWARES, NODE> {
    void init(String name, Connection connection) throws JMSException;
    void uninit();
}

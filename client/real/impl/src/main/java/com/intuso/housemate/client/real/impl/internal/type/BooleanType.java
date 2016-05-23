package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Type for a boolean
 */
public class BooleanType extends RealSimpleType<Boolean> {

    public final static TypeSerialiser<Boolean> SERIALISER = new TypeSerialiser<Boolean>() {
        @Override
        public Instance serialise(Boolean b) {
            return b != null ? new Instance(b.toString()) : null;
        }

        @Override
        public Boolean deserialise(Instance value) {
            return value != null && value.getValue() != null ? Boolean.parseBoolean(value.getValue()) : null;
        }
    };

    private final static Logger logger = LoggerFactory.getLogger(BooleanType.class);

    @Inject
    public BooleanType(ListenersFactory listenersFactory) {
        super(logger, Simple.Boolean, SERIALISER, listenersFactory);
    }
}

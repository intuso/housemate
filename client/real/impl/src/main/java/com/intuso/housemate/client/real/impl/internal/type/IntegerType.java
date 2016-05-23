package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Type for an integer
 */
public class IntegerType extends RealSimpleType<Integer> {

    public final static TypeSerialiser<Integer> SERIALISER = new TypeSerialiser<Integer>() {
        @Override
        public Instance serialise(Integer i) {
            return i != null ? new Instance(i.toString()) : null;
        }

        @Override
        public Integer deserialise(Instance value) {
            return value != null && value.getValue() != null ? new Integer(value.getValue()) : null;
        }
    };

    private final static Logger logger = LoggerFactory.getLogger(IntegerType.class);

    @Inject
    public IntegerType(ListenersFactory listenersFactory) {
        super(logger, Simple.Integer, SERIALISER, listenersFactory);
    }
}

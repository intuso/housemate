package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.TypeSerialiser;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for an integer
 */
public class IntegerType extends RealPrimitiveType<Integer> {

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

    @Inject
    public IntegerType(@Type Logger logger, ListenersFactory listenersFactory) {
        super(ChildUtil.logger(logger, Integer.class.getName()),
                new PrimitiveData(Integer.class.getName(), "Integer", "A whole number"),
                new TypeSpec(Integer.class),
                SERIALISER,
                listenersFactory);
    }
}

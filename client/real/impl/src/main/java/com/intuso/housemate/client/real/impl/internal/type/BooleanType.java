package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.TypeSerialiser;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class BooleanType extends RealPrimitiveType<Boolean> {

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

    @Inject
    public BooleanType(@Type Logger logger, ListenersFactory listenersFactory) {
        super(ChildUtil.logger(logger, Boolean.class.getName()),
                new PrimitiveData(Boolean.class.getName(), "Boolean", "True or false"),
                new TypeSpec(Boolean.class),
                SERIALISER,
                listenersFactory);
    }
}

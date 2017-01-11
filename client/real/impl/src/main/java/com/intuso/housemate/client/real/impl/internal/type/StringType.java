package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.TypeSerialiser;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for a string
 */
public class StringType extends RealPrimitiveType<String> {

    public final static TypeSerialiser<String> SERIALISER = new TypeSerialiser<String>() {
        @Override
        public Instance serialise(String s) {
            return s != null ? new Instance(s) : null;
        }

        @Override
        public String deserialise(Instance value) {
            return value != null ? value.getValue() : null;
        }
    };

    @Inject
    public StringType(@Type Logger logger, ListenersFactory listenersFactory) {
        super(ChildUtil.logger(logger, String.class.getName()),
                new PrimitiveData(String.class.getName(), "String", "Some text"),
                new TypeSpec(String.class),
                SERIALISER,
                listenersFactory);
    }
}

package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.TypeSerialiser;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for a double
 */
public class DoubleType extends RealPrimitiveType<Double> {

    public final static TypeSerialiser<Double> SERIALISER = new TypeSerialiser<Double>() {
        @Override
        public Instance serialise(Double d) {
            return d != null ? new Instance(d.toString()) : null;
        }

        @Override
        public Double deserialise(Instance value) {
            return value != null && value.getValue() != null ? new Double(value.getValue()) : null;
        }
    };

    @Inject
    public DoubleType(@Type Logger logger, ListenersFactory listenersFactory) {
        super(ChildUtil.logger(logger, Double.class.getName()),
                new PrimitiveData(Double.class.getName(), "Double", "A number with a decimal point"),
                new TypeSpec(Double.class),
                SERIALISER,
                listenersFactory);
    }
}

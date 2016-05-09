package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.TypeSerialiser;
import com.intuso.housemate.client.api.internal.object.Parameter;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealValueImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    /**
     * Creates an integer value object
     * @param logger the log
     * @param value the initial value
     * @return an integer value object
     */
    public static RealValueImpl<Integer> createValue(Logger logger, Value.Data data, ListenersFactory listenersFactory, Integer value) {
        return new RealValueImpl<>(logger, data, listenersFactory, new IntegerType(listenersFactory), value);
    }

    /**
     * Creates an integer property object
     * @param logger the log
     * @param values the initial values
     * @return an integer property object
     */
    public static RealPropertyImpl<Integer> createProperty(Logger logger, Property.Data data, ListenersFactory listenersFactory, List<Integer> values) {
        return new RealPropertyImpl<>(logger, data, listenersFactory, new IntegerType(listenersFactory), values);
    }

    /**
     * Creates an integer parameter object
     * @param logger the log
     * @return an integer parameter object
     */
    public static RealParameterImpl<Integer> createParameter(Logger logger, Parameter.Data data, ListenersFactory listenersFactory) {
        return new RealParameterImpl<>(logger, data, listenersFactory, new IntegerType(listenersFactory));
    }
}

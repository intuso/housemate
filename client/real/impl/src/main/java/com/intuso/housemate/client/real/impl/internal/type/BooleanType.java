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

    /**
     * Creates a boolean value object
     *  @param logger the log
     * @param listenersFactory
     * @param value the initial value     @return a boolean value object
     */
    public static RealValueImpl<Boolean> createValue(Logger logger, Value.Data data, ListenersFactory listenersFactory, Boolean value) {
        return new RealValueImpl<>(logger, data, listenersFactory, new BooleanType(listenersFactory), value);
    }

    /**
     * Creates a boolean property object
     * @param logger the log
     * @param values the initial values
     * @return a boolean property object
     */
    public static RealPropertyImpl<Boolean> createProperty(Logger logger, Property.Data data, ListenersFactory listenersFactory, List<Boolean> values) {
        return new RealPropertyImpl<>(logger, data, listenersFactory, new BooleanType(listenersFactory), values);
    }

    /**
     * Creates a boolean parameter object
     * @param logger the log
     * @return a boolean parameter object
     */
    public static RealParameterImpl<Boolean> createParameter(Logger logger, Parameter.Data data, ListenersFactory listenersFactory) {
        return new RealParameterImpl<>(logger, data, listenersFactory, new BooleanType(listenersFactory));
    }
}

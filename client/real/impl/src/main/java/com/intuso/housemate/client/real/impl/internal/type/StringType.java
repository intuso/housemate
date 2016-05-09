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
 * Type for a string
 */
public class StringType extends RealSimpleType<String> {

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

    private final static Logger logger = LoggerFactory.getLogger(StringType.class);

    @Inject
    public StringType(ListenersFactory listenersFactory) {
        super(logger, Simple.String, SERIALISER, listenersFactory);
    }

    /**
     * Creates an string value object
     * @param logger the log
     * @param value the initial value
     * @return a string value object
     */
    public static RealValueImpl<String> createValue(Logger logger, Value.Data data, ListenersFactory listenersFactory, String value) {
        return new RealValueImpl<>(logger, data, listenersFactory, new StringType(listenersFactory), value);
    }

    /**
     * Creates a string property object
     * @param logger the log
     * @param values the initial values
     * @return a string property object
     */
    public static RealPropertyImpl<String> createProperty(Logger logger, Property.Data data, ListenersFactory listenersFactory, List<String> values) {
        return new RealPropertyImpl<>(logger, data, listenersFactory, new StringType(listenersFactory), values);
    }

    /**
     * Creates a string parameter object
     * @param logger the log
     * @return a string parameter object
     */
    public static RealParameterImpl<String> createParameter(Logger logger, Parameter.Data data, ListenersFactory listenersFactory) {
        return new RealParameterImpl<>(logger, data, listenersFactory, new StringType(listenersFactory));
    }
}

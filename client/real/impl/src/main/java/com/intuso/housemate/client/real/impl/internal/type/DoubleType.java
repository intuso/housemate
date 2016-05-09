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
 * Type for a double
 */
public class DoubleType extends RealSimpleType<Double> {

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

    private final static Logger logger = LoggerFactory.getLogger(DoubleType.class);

    @Inject
    public DoubleType(ListenersFactory listenersFactory) {
        super(logger, Simple.Double, SERIALISER, listenersFactory);
    }

    /**
     * Creates a double value object
     * @param logger the log
     * @param value the initial value
     * @return a double value object
     */
    public static RealValueImpl<Double> createValue(Logger logger, Value.Data data, ListenersFactory listenersFactory, Double value) {
        return new RealValueImpl<>(logger, data, listenersFactory, new DoubleType(listenersFactory), value);
    }

    /**
     * Creates a double property object
     * @param logger the log
     * @param values the initial values
     * @return a double property object
     */
    public static RealPropertyImpl<Double> createProperty(Logger logger, Property.Data data, ListenersFactory listenersFactory, List<Double> values) {
        return new RealPropertyImpl<>(logger, data, listenersFactory, new DoubleType(listenersFactory), values);
    }

    /**
     * Creates a double parameter object
     * @param logger the log
     * @return a double parameter object
     */
    public static RealParameterImpl<Double> createParameter(Logger logger, Parameter.Data data, ListenersFactory listenersFactory) {
        return new RealParameterImpl<>(logger, data, listenersFactory, new DoubleType(listenersFactory));
    }
}

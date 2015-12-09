package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealValueImpl;
import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * Type for a double
 */
public class DoubleType extends RealSimpleType<Double> {

    public final static TypeSerialiser<Double> SERIALISER = new TypeSerialiser<Double>() {
        @Override
        public TypeInstance serialise(Double d) {
            return d != null ? new TypeInstance(d.toString()) : null;
        }

        @Override
        public Double deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? new Double(value.getValue()) : null;
        }
    };

    /**
     * @param logger {@inheritDoc}
     */
    @Inject
    public DoubleType(Logger logger, ListenersFactory listenersFactory) {
        super(logger, listenersFactory, SimpleTypeData.Type.Double, SERIALISER);
    }

    /**
     * Creates a double value object
     * @param logger the logger
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value
     * @return a double value object
     */
    public static RealValueImpl<Double> createValue(Logger logger, ListenersFactory listenersFactory,
                                                String id, String name, String description, Double value) {
        return new RealValueImpl<>(logger, listenersFactory, id, name, description, new DoubleType(logger, listenersFactory), value);
    }

    /**
     * Creates a double property object
     * @param logger the logger
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param values the initial values
     * @return a double property object
     */
    public static RealPropertyImpl<Double> createProperty(Logger logger, ListenersFactory listenersFactory,
                                                      String id, String name, String description, List<Double> values) {
        return new RealPropertyImpl<>(logger, listenersFactory, id, name, description, new DoubleType(logger, listenersFactory), values);
    }

    /**
     * Creates a double parameter object
     * @param logger the logger
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return a double parameter object
     */
    public static RealParameterImpl<Double> createParameter(Logger logger, ListenersFactory listenersFactory,
                                                        String id, String name, String description) {
        return new RealParameterImpl<>(logger,listenersFactory , id, name, description, new DoubleType(logger, listenersFactory));
    }
}

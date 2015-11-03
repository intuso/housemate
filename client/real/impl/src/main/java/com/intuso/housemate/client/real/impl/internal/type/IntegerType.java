package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealValueImpl;
import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Type for an integer
 */
public class IntegerType extends RealSimpleType<Integer> {

    public final static TypeSerialiser<Integer> SERIALISER = new TypeSerialiser<Integer>() {
        @Override
        public TypeInstance serialise(Integer i) {
            return i != null ? new TypeInstance(i.toString()) : null;
        }

        @Override
        public Integer deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? new Integer(value.getValue()) : null;
        }
    };

    /**
     * @param log {@inheritDoc}
     */
    @Inject
    public IntegerType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, SimpleTypeData.Type.Integer, SERIALISER);
    }

    /**
     * Creates an integer value object
     * @param log the log
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value
     * @return an integer value object
     */
    public static RealValueImpl<Integer> createValue(Log log, ListenersFactory listenersFactory,
                                                 String id, String name, String description, Integer value) {
        return new RealValueImpl<>(log, listenersFactory, id, name, description, new IntegerType(log, listenersFactory), value);
    }

    /**
     * Creates an integer property object
     * @param log the log
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param values the initial values
     * @return an integer property object
     */
    public static RealPropertyImpl<Integer> createProperty(Log log, ListenersFactory listenersFactory,
                                                       String id, String name, String description, List<Integer> values) {
        return new RealPropertyImpl<>(log, listenersFactory, id, name, description, new IntegerType(log, listenersFactory), values);
    }

    /**
     * Creates an integer parameter object
     * @param log the log
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return an integer parameter object
     */
    public static RealParameterImpl<Integer> createParameter(Log log, ListenersFactory listenersFactory,
                                                         String id, String name, String description) {
        return new RealParameterImpl<>(log, listenersFactory, id, name, description, new IntegerType(log, listenersFactory));
    }
}

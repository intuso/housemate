package com.intuso.housemate.object.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealValue;
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
    public static RealValue<Integer> createValue(Log log, ListenersFactory listenersFactory,
                                                 String id, String name, String description, Integer value) {
        return new RealValue<>(log, listenersFactory, id, name, description, new IntegerType(log, listenersFactory), value);
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
    public static RealProperty<Integer> createProperty(Log log, ListenersFactory listenersFactory,
                                                       String id, String name, String description, List<Integer> values) {
        return new RealProperty<>(log, listenersFactory, id, name, description, new IntegerType(log, listenersFactory), values);
    }

    /**
     * Creates an integer parameter object
     * @param log the log
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return an integer parameter object
     */
    public static RealParameter<Integer> createParameter(Log log, ListenersFactory listenersFactory,
                                                         String id, String name, String description) {
        return new RealParameter<>(log, listenersFactory, id, name, description, new IntegerType(log, listenersFactory));
    }
}

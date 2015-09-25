package com.intuso.housemate.client.real.api.internal.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealParameter;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Type for a boolean
 */
public class BooleanType extends RealSimpleType<Boolean> {

    public final static TypeSerialiser<Boolean> SERIALISER = new TypeSerialiser<Boolean>() {
        @Override
        public TypeInstance serialise(Boolean b) {
            return b != null ? new TypeInstance(b.toString()) : null;
        }

        @Override
        public Boolean deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? Boolean.parseBoolean(value.getValue()) : null;
        }
    };

    /**
     * @param log {@inheritDoc}
     */
    @Inject
    public BooleanType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, SimpleTypeData.Type.Boolean, SERIALISER);
    }

    /**
     * Creates a boolean value object
     *
     * @param log the log
     * @param listenersFactory
     *@param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value     @return a boolean value object
     */
    public static RealValue<Boolean> createValue(Log log, ListenersFactory listenersFactory,
                                                 String id, String name, String description, Boolean value) {
        return new RealValue<>(log, listenersFactory, id, name, description, new BooleanType(log, listenersFactory), value);
    }

    /**
     * Creates a boolean property object
     * @param log the log
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param values the initial values
     * @return a boolean property object
     */
    public static RealProperty<Boolean> createProperty(Log log, ListenersFactory listenersFactory,
                                                       String id, String name, String description, List<Boolean> values) {
        return new RealProperty<>(log, listenersFactory, id, name, description, new BooleanType(log, listenersFactory), values);
    }

    /**
     * Creates a boolean parameter object
     * @param log the log
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return a boolean parameter object
     */
    public static RealParameter<Boolean> createParameter(Log log, ListenersFactory listenersFactory,
                                                         String id, String name, String description) {
        return new RealParameter<>(log, listenersFactory, id, name, description, new BooleanType(log, listenersFactory));
    }
}

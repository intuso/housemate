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
import org.slf4j.LoggerFactory;

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

    private final static Logger logger = LoggerFactory.getLogger(BooleanType.class);

    @Inject
    public BooleanType(ListenersFactory listenersFactory) {
        super(logger, listenersFactory, SimpleTypeData.Type.Boolean, SERIALISER);
    }

    /**
     * Creates a boolean value object
     *
     * @param logger the logger
     * @param listenersFactory
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value     @return a boolean value object
     */
    public static RealValueImpl<Boolean> createValue(Logger logger, ListenersFactory listenersFactory,
                                                 String id, String name, String description, Boolean value) {
        return new RealValueImpl<>(listenersFactory, logger, id, name, description, new BooleanType(listenersFactory), value);
    }

    /**
     * Creates a boolean property object
     * @param logger the logger
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param values the initial values
     * @return a boolean property object
     */
    public static RealPropertyImpl<Boolean> createProperty(Logger logger, ListenersFactory listenersFactory,
                                                       String id, String name, String description, List<Boolean> values) {
        return new RealPropertyImpl<>(logger, listenersFactory, id, name, description, new BooleanType(listenersFactory), values);
    }

    /**
     * Creates a boolean parameter object
     * @param logger the logger
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return a boolean parameter object
     */
    public static RealParameterImpl<Boolean> createParameter(Logger logger, ListenersFactory listenersFactory,
                                                         String id, String name, String description) {
        return new RealParameterImpl<>(listenersFactory, logger, id, name, description, new BooleanType(listenersFactory));
    }
}

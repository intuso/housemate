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
 * Type for a string
 */
public class StringType extends RealSimpleType<String> {

    public final static TypeSerialiser<String> SERIALISER = new TypeSerialiser<String>() {
        @Override
        public TypeInstance serialise(String s) {
            return s != null ? new TypeInstance(s) : null;
        }

        @Override
        public String deserialise(TypeInstance value) {
            return value != null ? value.getValue() : null;
        }
    };

    private final static Logger logger = LoggerFactory.getLogger(StringType.class);

    @Inject
    public StringType(ListenersFactory listenersFactory) {
        super(logger, listenersFactory, SimpleTypeData.Type.String, SERIALISER);
    }

    /**
     * Creates an string value object
     * @param logger the logger
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value
     * @return a string value object
     */
    public static RealValueImpl<String> createValue(Logger logger, ListenersFactory listenersFactory,
                                                String id, String name, String description, String value) {
        return new RealValueImpl<>(listenersFactory, logger, id, name, description, new StringType(listenersFactory), value);
    }

    /**
     * Creates a string property object
     * @param logger the logger
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param values the initial values
     * @return a string property object
     */
    public static RealPropertyImpl<String> createProperty(Logger logger, ListenersFactory listenersFactory,
                                                      String id, String name, String description, List<String> values) {
        return new RealPropertyImpl<>(logger, listenersFactory, id, name, description, new StringType(listenersFactory), values);
    }

    /**
     * Creates a string parameter object
     * @param logger the logger
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return a string parameter object
     */
    public static RealParameterImpl<String> createParameter(Logger logger, ListenersFactory listenersFactory,
                                                        String id, String name, String description) {
        return new RealParameterImpl<>(listenersFactory, logger, id, name, description, new StringType(listenersFactory));
    }
}

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

    /**
     * @param log {@inheritDoc}
     */
    @Inject
    public StringType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, SimpleTypeData.Type.String, SERIALISER);
    }

    /**
     * Creates an string value object
     * @param log the log
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value
     * @return a string value object
     */
    public static RealValue<String> createValue(Log log, ListenersFactory listenersFactory,
                                                String id, String name, String description, String value) {
        return new RealValue<>(log, listenersFactory, id, name, description, new StringType(log, listenersFactory), value);
    }

    /**
     * Creates a string property object
     * @param log the log
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param values the initial values
     * @return a string property object
     */
    public static RealProperty<String> createProperty(Log log, ListenersFactory listenersFactory,
                                                      String id, String name, String description, List<String> values) {
        return new RealProperty<>(log, listenersFactory, id, name, description, new StringType(log, listenersFactory), values);
    }

    /**
     * Creates a string parameter object
     * @param log the log
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return a string parameter object
     */
    public static RealParameter<String> createParameter(Log log, ListenersFactory listenersFactory,
                                                        String id, String name, String description) {
        return new RealParameter<>(log, listenersFactory, id, name, description, new StringType(log, listenersFactory));
    }
}

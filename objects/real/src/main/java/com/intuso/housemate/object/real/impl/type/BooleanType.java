package com.intuso.housemate.object.real.impl.type;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealValue;
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
    public BooleanType(Log log) {
        super(log, SimpleTypeData.Type.Boolean, SERIALISER);
    }

    /**
     * Creates a boolean value object
     * @param log the log
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value
     * @return a boolean value object
     */
    public static RealValue<Boolean> createValue(Log log, String id, String name, String description, Boolean value) {
        return new RealValue<Boolean>(log, id, name, description, new BooleanType(log), value);
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
    public static RealProperty<Boolean> createProperty(Log log, String id, String name, String description, List<Boolean> values) {
        return new RealProperty<Boolean>(log, id, name, description, new BooleanType(log), values);
    }

    /**
     * Creates a boolean parameter object
     * @param log the log
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return a boolean parameter object
     */
    public static RealParameter<Boolean> createParameter(Log log, String id, String name, String description) {
        return new RealParameter<Boolean>(log, id, name, description, new BooleanType(log));
    }
}

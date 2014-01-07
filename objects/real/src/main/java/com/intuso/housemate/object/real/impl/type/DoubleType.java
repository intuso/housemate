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
     * @param log {@inheritDoc}
     */
    @Inject
    public DoubleType(Log log) {
        super(log, SimpleTypeData.Type.Double, SERIALISER);
    }

    /**
     * Creates a double value object
     * @param log the log
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value
     * @return a double value object
     */
    public static RealValue<Double> createValue(Log log, String id, String name, String description, Double value) {
        return new RealValue<Double>(log, id, name, description, new DoubleType(log), value);
    }

    /**
     * Creates a double property object
     * @param log the log
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param values the initial values
     * @return a double property object
     */
    public static RealProperty<Double> createProperty(Log log, String id, String name, String description, List<Double> values) {
        return new RealProperty<Double>(log, id, name, description, new DoubleType(log), values);
    }

    /**
     * Creates a double parameter object
     * @param log the log
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return a double parameter object
     */
    public static RealParameter<Double> createParameter(Log log, String id, String name, String description) {
        return new RealParameter<Double>(log, id, name, description, new DoubleType(log));
    }
}

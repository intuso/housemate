package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.type.SimpleTypeWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealValue;

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
     * @param resources {@inheritDoc}
     */
    public IntegerType(RealResources resources) {
        super(resources, SimpleTypeWrappable.Type.Integer, SERIALISER);
    }

    /**
     * Creates an integer value object
     * @param resources the resources
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param value the initial value
     * @return an integer value object
     */
    public static RealValue<Integer> createValue(RealResources resources, String id, String name, String description, Integer value) {
        return new RealValue<Integer>(resources, id, name, description, new IntegerType(resources), value);
    }

    /**
     * Creates an integer property object
     * @param resources the resources
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param values the initial values
     * @return an integer property object
     */
    public static RealProperty<Integer> createProperty(RealResources resources, String id, String name, String description, List<Integer> values) {
        return new RealProperty<Integer>(resources, id, name, description, new IntegerType(resources), values);
    }

    /**
     * Creates an integer parameter object
     * @param resources the resources
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @return an integer parameter object
     */
    public static RealParameter<Integer> createParameter(RealResources resources, String id, String name, String description) {
        return new RealParameter<Integer>(resources, id, name, description, new IntegerType(resources));
    }
}

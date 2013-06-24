package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.type.SimpleTypeWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealValue;

/**
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

    public IntegerType(RealResources resources) {
        super(resources, SimpleTypeWrappable.Type.Integer, SERIALISER);
    }

    public static RealValue<Integer> createValue(RealResources resources, String id, String name, String description, Integer value) {
        return new RealValue<Integer>(resources, id, name, description, new IntegerType(resources), value);
    }

    public static RealProperty<Integer> createProperty(RealResources resources, String id, String name, String description, Integer value) {
        return new RealProperty<Integer>(resources, id, name, description, new IntegerType(resources), value);
    }

    public static RealParameter<Integer> createParameter(RealResources resources, String id, String name, String description) {
        return new RealParameter<Integer>(resources, id, name, description, new IntegerType(resources));
    }
}

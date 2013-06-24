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

    public BooleanType(RealResources resources) {
        super(resources, SimpleTypeWrappable.Type.Boolean, SERIALISER);
    }

    public static RealValue<Boolean> createValue(RealResources resources, String id, String name, String description, Boolean value) {
        return new RealValue<Boolean>(resources, id, name, description, new BooleanType(resources), value);
    }

    public static RealProperty<Boolean> createProperty(RealResources resources, String id, String name, String description, Boolean value) {
        return new RealProperty<Boolean>(resources, id, name, description, new BooleanType(resources), value);
    }

    public static RealParameter<Boolean> createParameter(RealResources resources, String id, String name, String description) {
        return new RealParameter<Boolean>(resources, id, name, description, new BooleanType(resources));
    }
}

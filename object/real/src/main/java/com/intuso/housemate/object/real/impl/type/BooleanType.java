package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.type.SimpleTypeWrappable;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealArgument;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealValue;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class BooleanType extends RealSimpleType<Boolean> {

    public final static TypeSerialiser<Boolean> SERIALISER = new TypeSerialiser<Boolean>() {
        @Override
        public String serialise(Boolean b) {
            return b.toString();
        }

        @Override
        public Boolean deserialise(String value) {
            return Boolean.parseBoolean(value);
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

    public static RealArgument<Boolean> createArgument(RealResources resources, String id, String name, String description) {
        return new RealArgument<Boolean>(resources, id, name, description, new BooleanType(resources));
    }
}

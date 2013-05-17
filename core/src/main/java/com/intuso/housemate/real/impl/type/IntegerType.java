package com.intuso.housemate.real.impl.type;

import com.intuso.housemate.core.object.type.SimpleTypeWrappable;
import com.intuso.housemate.core.object.type.TypeSerialiser;
import com.intuso.housemate.real.RealArgument;
import com.intuso.housemate.real.RealProperty;
import com.intuso.housemate.real.RealResources;
import com.intuso.housemate.real.RealValue;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class IntegerType extends RealSimpleType<Integer> {

    public final static TypeSerialiser<Integer> SERIALISER = new TypeSerialiser<Integer>() {
        @Override
        public String serialise(Integer i) {
            return i.toString();
        }

        @Override
        public Integer deserialise(String value) {
            return new Integer(value);
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

    public static RealArgument<Integer> createArgument(RealResources resources, String id, String name, String description) {
        return new RealArgument<Integer>(resources, id, name, description, new IntegerType(resources));
    }
}

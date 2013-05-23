package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.type.SimpleTypeWrappable;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealArgument;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealValue;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class StringType extends RealSimpleType<String> {

    public final static TypeSerialiser<String> SERIALISER = new TypeSerialiser<String>() {
        @Override
        public String serialise(String s) {
            return s;
        }

        @Override
        public String deserialise(String value) {
            return value;
        }
    };

    public StringType(RealResources resources) {
        super(resources, SimpleTypeWrappable.Type.String, SERIALISER);
    }

    public static RealValue<String> createValue(RealResources resources, String id, String name, String description, String value) {
        return new RealValue<String>(resources, id, name, description, new StringType(resources), value);
    }

    public static RealProperty<String> createProperty(RealResources resources, String id, String name, String description, String value) {
        return new RealProperty<String>(resources, id, name, description, new StringType(resources), value);
    }

    public static RealArgument<String> createArgument(RealResources resources, String id, String name, String description) {
        return new RealArgument<String>(resources, id, name, description, new StringType(resources));
    }
}

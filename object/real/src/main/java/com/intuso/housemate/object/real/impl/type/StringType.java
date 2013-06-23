package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.type.SimpleTypeWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealResources;
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
        public TypeInstance serialise(String s) {
            return s != null ? new TypeInstance(s) : null;
        }

        @Override
        public String deserialise(TypeInstance value) {
            return value != null ? value.getValue() : null;
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

    public static RealParameter<String> createParameter(RealResources resources, String id, String name, String description) {
        return new RealParameter<String>(resources, id, name, description, new StringType(resources));
    }
}

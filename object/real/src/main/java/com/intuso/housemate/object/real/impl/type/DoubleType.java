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

    public DoubleType(RealResources resources) {
        super(resources, SimpleTypeWrappable.Type.Double, SERIALISER);
    }

    public static RealValue<Double> createValue(RealResources resources, String id, String name, String description, Double value) {
        return new RealValue<Double>(resources, id, name, description, new DoubleType(resources), value);
    }

    public static RealProperty<Double> createProperty(RealResources resources, String id, String name, String description, Double value) {
        return new RealProperty<Double>(resources, id, name, description, new DoubleType(resources), value);
    }

    public static RealParameter<Double> createParameter(RealResources resources, String id, String name, String description) {
        return new RealParameter<Double>(resources, id, name, description, new DoubleType(resources));
    }
}

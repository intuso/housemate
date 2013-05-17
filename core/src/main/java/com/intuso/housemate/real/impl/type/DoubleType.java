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
public class DoubleType extends RealSimpleType<Double> {

    public final static TypeSerialiser<Double> SERIALISER = new TypeSerialiser<Double>() {
        @Override
        public String serialise(Double d) {
            return d.toString();
        }

        @Override
        public Double deserialise(String value) {
            return new Double(value);
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

    public static RealArgument<Double> createArgument(RealResources resources, String id, String name, String description) {
        return new RealArgument<Double>(resources, id, name, description, new DoubleType(resources));
    }
}

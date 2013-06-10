package com.intuso.housemate.broker.plugin.type;

import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.housemate.object.real.impl.type.RealSingleChoiceType;
import com.intuso.housemate.object.real.impl.type.StringType;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 03/06/13
 * Time: 07:20
 * To change this template use File | Settings | File Templates.
 */
public class ValueSourceType extends RealSingleChoiceType<ValueSource> {

    public final static String ID = "value-source";
    public final static String NAME = "Value Source";
    public final static String DESCRIPTION = "The source for a value";

    public final static String DYNAMIC_ID = "dynamic";
    public final static String DYNAMIC_NAME = "Dynamic";
    public final static String DYNAMIC_DESCRIPTION = "A changing value from another part of the system";

    public final static String STATIC_ID = "static";
    public final static String STATIC_NAME = "Static";
    public final static String STATIC_DESCRIPTION = "A static value";

    private final RealObjectType<Value<?, ?>> realObjectType;
    private final Root<?, ?> root;

    public ValueSourceType(RealResources resources, Root<?, ?> root) {
        super(resources, ID, NAME, DESCRIPTION, createOptions(resources, root));
        this.root = root;
        realObjectType = new RealObjectType<Value<?, ?>>(resources, root);
    }

    @Override
    public TypeInstance serialise(ValueSource source) {
        if(source instanceof StaticValue) {
            TypeInstances childValues = new TypeInstances();
            childValues.put("value", source.getValue().getTypeInstance());
            return new TypeInstance(STATIC_ID, childValues);
        } else if(source instanceof DynamicValue) {
            TypeInstances childValues = new TypeInstances();
            childValues.put("path", realObjectType.serialise(((DynamicValue) source).getObjectReference()));
            return new TypeInstance(DYNAMIC_ID, childValues);
        } else
            return null;
    }

    @Override
    public ValueSource deserialise(TypeInstance value) {
        if(value == null || value.getValue() == null)
            return null;
        if(value.getValue().equals(STATIC_ID)) {
            return new StaticValue(value.getChildValues().get("value"));
        } else if(value.getValue().equals(DYNAMIC_ID)) {
            return new DynamicValue(realObjectType.deserialise(value.getChildValues().get("path")), root);
        } else
            return null;
    }

    private static List<RealOption> createOptions(RealResources resources, Root<?, ?> root) {
        return Arrays.asList(
                new RealOption(resources, STATIC_ID, STATIC_NAME, STATIC_DESCRIPTION,
                        Arrays.<RealSubType<?>>asList(
                                new RealSubType<String>(resources, "value", "Value", "Value", new StringType(resources)))),
                new RealOption(resources, DYNAMIC_ID, DYNAMIC_NAME, DYNAMIC_DESCRIPTION,
                        Arrays.<RealSubType<?>>asList(
                                new RealSubType<RealObjectType.Reference>(resources, "path", "Path", "Path to the value", new RealObjectType(resources, root)))));
    }
}

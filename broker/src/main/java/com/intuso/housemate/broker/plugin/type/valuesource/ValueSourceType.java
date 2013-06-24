package com.intuso.housemate.broker.plugin.type.valuesource;

import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.broker.plugin.MainPlugin;
import com.intuso.housemate.broker.plugin.type.constant.ConstantInstance;
import com.intuso.housemate.broker.plugin.type.constant.ConstantType;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.housemate.object.real.impl.type.RealSingleChoiceType;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 */
public class ValueSourceType extends RealSingleChoiceType<ValueSource> {

    public final static String ID = "value-source";
    public final static String NAME = "Value Source";
    public final static String DESCRIPTION = "The source for a value";

    public final static String VALUE_LOCATION_ID = "location";
    public final static String VALUE_LOCATION_NAME = "Location";
    public final static String VALUE_LOCATION_DESCRIPTION = "The location of another value";

    public final static String CONSTANT_VALUE_ID = "constant";
    public final static String CONSTANT_VALUE_NAME = "Constant";
    public final static String CONSTANT_VALUE_DESCRIPTION = "A user-defined value";

    private final Serialiser serialiser;

    public ValueSourceType(RealResources resources, Root<?, ?> root, RealList<TypeWrappable<?>, RealType<?, ?, ?>> types) {
        super(resources, ID, NAME, DESCRIPTION, createOptions(resources, root));
        serialiser = new Serialiser(resources.getLog(), new RealObjectType.Serialiser<Value<?, ?>>(root), root, types);
    }

    private static List<RealOption> createOptions(RealResources resources, Root<?, ?> root) {
        return Arrays.asList(
                new RealOption(resources, CONSTANT_VALUE_ID, CONSTANT_VALUE_NAME, CONSTANT_VALUE_DESCRIPTION,
                        Arrays.<RealSubType<?>>asList(
                                new RealSubType<ConstantInstance<Object>>(resources, "type", "Type", "Type of the value", MainPlugin.CONSTANT_TYPE))),
                new RealOption(resources, VALUE_LOCATION_ID, VALUE_LOCATION_NAME, VALUE_LOCATION_DESCRIPTION,
                        Arrays.<RealSubType<?>>asList(
                                new RealSubType<RealObjectType.Reference>(resources, "path", "Path", "Path to the value", new RealObjectType(resources, root)))));
    }

    @Override
    public TypeInstance serialise(ValueSource o) {
        return serialiser.serialise(o);
    }

    @Override
    public ValueSource deserialise(TypeInstance instance) {
        return serialiser.deserialise(instance);
    }

    public static class Serialiser implements TypeSerialiser<ValueSource> {

        private final Log log;
        private final RealObjectType.Serialiser<Value<?, ?>> realObjectTypeSerialiser;
        private final Root<?, ?> root;
        private final RealList<TypeWrappable<?>, RealType<?, ?, ?>> types;

        public Serialiser(Log log, RealObjectType.Serialiser<Value<?, ?>> realObjectTypeSerialiser, Root<?, ?> root,
                          RealList<TypeWrappable<?>, RealType<?, ?, ?>> types) {
            this.log = log;
            this.realObjectTypeSerialiser = realObjectTypeSerialiser;
            this.root = root;
            this.types = types;
        }

        @Override
        public TypeInstance serialise(ValueSource source) {
            if(source instanceof ConstantValue) {
                TypeInstances childValues = new TypeInstances();
                TypeInstance typeInstance = new TypeInstance(source.getValue().getType().getId());
                typeInstance.getChildValues().put(ConstantType.SUB_TYPE_ID, source.getValue().getTypeInstance());
                childValues.put("type", typeInstance);
                return new TypeInstance(CONSTANT_VALUE_ID, childValues);
            } else if(source instanceof ValueLocation) {
                TypeInstances childValues = new TypeInstances();
                childValues.put("path", realObjectTypeSerialiser.serialise(((ValueLocation) source).getObjectReference()));
                return new TypeInstance(VALUE_LOCATION_ID, childValues);
            } else
                return null;
        }

        @Override
        public ValueSource deserialise(TypeInstance value) {
            if(value == null || value.getValue() == null)
                return null;
            if(value.getValue().equals(CONSTANT_VALUE_ID)) {
                TypeInstance typeValue = value.getChildValues().get("type");
                if(typeValue == null)
                    return null;
                String typeId = typeValue.getValue();
                if(typeId == null) {
                    log.w("Cannot deserialise constant, type id is null");
                    return null;
                }
                RealType<?, ?, ?> type = types.get(typeId);
                if(type == null) {
                    log.w("Cannot deserialise constant, no type for id " + typeId);
                    return null;
                }
                return new ConstantValue((RealType<?,?,Object>) type, typeValue.getChildValues().get(ConstantType.SUB_TYPE_ID));
            } else if(value.getValue().equals(VALUE_LOCATION_ID)) {
                return new ValueLocation(realObjectTypeSerialiser.deserialise(value.getChildValues().get("path")), root);
            } else
                return null;
        }
    }
}

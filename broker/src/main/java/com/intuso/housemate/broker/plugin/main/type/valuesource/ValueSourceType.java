package com.intuso.housemate.broker.plugin.main.type.valuesource;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.broker.plugin.main.type.constant.ConstantInstance;
import com.intuso.housemate.broker.plugin.main.type.constant.ConstantType;
import com.intuso.housemate.broker.plugin.main.type.operation.Operation;
import com.intuso.housemate.broker.plugin.main.type.operation.OperationType;
import com.intuso.housemate.broker.plugin.main.type.transformation.Transformation;
import com.intuso.housemate.broker.plugin.main.type.transformation.TransformationType;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 */
@Singleton
public class ValueSourceType extends RealChoiceType<ValueSource> {

    public final static String ID = "value-source";
    public final static String NAME = "Value Source";
    public final static String DESCRIPTION = "The source for a value";

    public final static String CONSTANT_ID = "constant";
    public final static String CONSTANT_NAME = "Constant";
    public final static String CONSTANT_DESCRIPTION = "A user-defined value";

    public final static String LOCATION_ID = "location";
    public final static String LOCATION_NAME = "Location";
    public final static String LOCATION_DESCRIPTION = "The location of another value";

    public final static String OPERATION_ID = "operation";
    public final static String OPERATION_NAME = "Operation";
    public final static String OPERATION_DESCRIPTION = "The result of an operation";

    public final static String TRANSFORMATION_ID = "transformation";
    public final static String TRANSFORMATION_NAME = "Transformation";
    public final static String TRANSFORMATION_DESCRIPTION = "The transformation of another value";

    private final TypeSerialiser<ValueSource> serialiser;

    @Inject
    public ValueSourceType(RealResources resources, TypeSerialiser<ValueSource> serialiser,
                           RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        super(resources, ID, NAME, DESCRIPTION, 1, 1, createOptions(resources, types));
        this.serialiser = serialiser;
    }

    private static List<RealOption> createOptions(RealResources resources, RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        return Arrays.asList(
                new RealOption(resources, CONSTANT_ID, CONSTANT_NAME, CONSTANT_DESCRIPTION,
                        Arrays.<RealSubType<?>>asList(
                                new RealSubType<ConstantInstance<Object>>(resources, "type", "Type", "Type of the value", ConstantType.ID, types))),
                new RealOption(resources, LOCATION_ID, LOCATION_NAME, LOCATION_DESCRIPTION,
                        Arrays.<RealSubType<?>>asList(
                                new RealSubType<RealObjectType.Reference<Value<?, ?>>>(resources, "path", "Path", "Path to the value", RealObjectType.ID, types))),
                new RealOption(resources, OPERATION_ID, OPERATION_NAME, OPERATION_DESCRIPTION,
                        Arrays.<RealSubType<?>>asList(
                                new RealSubType<Operation>(resources, "operation", "Operation", "The operation to do", OperationType.ID, types))),
                new RealOption(resources, TRANSFORMATION_ID, TRANSFORMATION_NAME, TRANSFORMATION_DESCRIPTION,
                        Arrays.<RealSubType<?>>asList(
                                new RealSubType<Transformation>(resources, "path", "Path", "Path to the value", TransformationType.ID, types))));
    }

    @Override
    public TypeInstance serialise(ValueSource o) {
        return serialiser.serialise(o);
    }

    @Override
    public ValueSource deserialise(TypeInstance instance) {
        return serialiser.deserialise(instance);
    }

    @Singleton
    public static class Serialiser implements TypeSerialiser<ValueSource> {

        private final Log log;
        private final TypeSerialiser<RealObjectType.Reference<?>> realObjectTypeSerialiser;
        private final TypeSerialiser<Operation> operationSerialiser;
        private final TypeSerialiser<Transformation> transformationSerialiser;
        private final Root<?> root;
        private final RealList<TypeData<?>, RealType<?, ?, ?>> types;

        @Inject
        public Serialiser(Log log,
                          TypeSerialiser<RealObjectType.Reference<?>> realObjectTypeSerialiser,
                          TypeSerialiser<Operation> operationSerialiser, TypeSerialiser<Transformation> transformationSerialiser,
                          Root<?> root, RealList<TypeData<?>, RealType<?, ?, ?>> types) {
            this.log = log;
            this.realObjectTypeSerialiser = realObjectTypeSerialiser;
            this.operationSerialiser = operationSerialiser;
            this.transformationSerialiser = transformationSerialiser;
            this.root = root;
            this.types = types;
        }

        @Override
        public TypeInstance serialise(ValueSource source) {
            if(source instanceof ConstantValue) {
                TypeInstanceMap childValues = new TypeInstanceMap();
                TypeInstance typeInstance = new TypeInstance(source.getValue().getType().getId());
                typeInstance.getChildValues().put(ConstantType.SUB_TYPE_ID, source.getValue().getTypeInstances());
                childValues.put("type", new TypeInstances(typeInstance));
                return new TypeInstance(CONSTANT_ID, childValues);
            } else if(source instanceof ValueLocation) {
                TypeInstanceMap childValues = new TypeInstanceMap();
                childValues.put("path", new TypeInstances(realObjectTypeSerialiser.serialise(((ValueLocation) source).getObjectReference())));
                return new TypeInstance(LOCATION_ID, childValues);
            } else if(source instanceof OperationOutput) {
                TypeInstanceMap childValues = new TypeInstanceMap();
                childValues.put("operation", new TypeInstances(operationSerialiser.serialise(((OperationOutput)source).getOperation())));
                return new TypeInstance(OPERATION_ID, childValues);
            } else if(source instanceof TransformationOutput) {
                TypeInstanceMap childValues = new TypeInstanceMap();
                childValues.put("transformation", new TypeInstances(transformationSerialiser.serialise(((TransformationOutput)source).getTransformation())));
                return new TypeInstance(TRANSFORMATION_ID, childValues);
            } else
                return null;
        }

        @Override
        public ValueSource deserialise(TypeInstance value) {
            if(value == null || value.getValue() == null)
                return null;
            if(value.getValue().equals(CONSTANT_ID)) {
                TypeInstances typeValue = value.getChildValues().get("type");
                if(typeValue == null)
                    return null;
                String typeId = typeValue.getFirstValue();
                if(typeId == null) {
                    log.w("Cannot deserialise constant, type id is null");
                    return null;
                }
                RealType<?, ?, ?> type = types.get(typeId);
                if(type == null) {
                    log.w("Cannot deserialise constant, no type for id " + typeId);
                    return null;
                }
                return new ConstantValue((RealType<?,?,Object>) type, typeValue.get(0).getChildValues().get(ConstantType.SUB_TYPE_ID));
            } else if(value.getValue().equals(LOCATION_ID)) {
                if(value.getChildValues().get("path") != null && value.getChildValues().get("path").size() > 0) {
                    RealObjectType.Reference<?> object = realObjectTypeSerialiser.deserialise(value.getChildValues().get("path").get(0));
                    return new ValueLocation((RealObjectType.Reference<Value<?, ?>>)object, root);
                } else
                    return null;
            } else if(value.getValue().equals(OPERATION_ID)) {
                if(value.getChildValues().get("operation") != null && value.getChildValues().get("operation").size() > 0)
                    return new OperationOutput(log, types, operationSerialiser.deserialise(value.getChildValues().get("operation").get(0)));
                else
                    return null;
            } else if(value.getValue().equals(TRANSFORMATION_ID)) {
                if(value.getChildValues().get("transformation") != null && value.getChildValues().get("transformation").size() > 0)
                    return new TransformationOutput(log, types, transformationSerialiser.deserialise(value.getChildValues().get("transformation").get(0)));
                else
                    return null;
            } else
                return null;
        }
    }
}

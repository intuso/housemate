package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.plugin.main.type.constant.ConstantInstance;
import com.intuso.housemate.server.plugin.main.type.constant.ConstantType;
import com.intuso.housemate.server.plugin.main.type.operation.Operation;
import com.intuso.housemate.server.plugin.main.type.operation.OperationType;
import com.intuso.housemate.server.plugin.main.type.transformation.Transformation;
import com.intuso.housemate.server.plugin.main.type.transformation.TransformationType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 */
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
    public ValueSourceType(Log log, ListenersFactory listenersFactory, TypeSerialiser<ValueSource> serialiser,
                           RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        super(log, listenersFactory, ID, NAME, DESCRIPTION, 1, 1, createOptions(log, listenersFactory, types));
        this.serialiser = serialiser;
    }

    private static List<RealOption> createOptions(Log log, ListenersFactory listenersFactory, RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        return Arrays.asList(
                new RealOption(log, listenersFactory, CONSTANT_ID, CONSTANT_NAME,
                        CONSTANT_DESCRIPTION, Arrays.<RealSubType<?>>asList(
                                new RealSubType<ConstantInstance<Object>>(log, listenersFactory, "type", "Type", "Type of the value", ConstantType.ID, types))),
                new RealOption(log, listenersFactory, LOCATION_ID, LOCATION_NAME,
                        LOCATION_DESCRIPTION, Arrays.<RealSubType<?>>asList(
                                new RealSubType<RealObjectType.Reference<Value<?, ?>>>(log, listenersFactory, "path", "Path", "Path to the value", RealObjectType.ID, types))),
                new RealOption(log, listenersFactory, OPERATION_ID, OPERATION_NAME,
                        OPERATION_DESCRIPTION, Arrays.<RealSubType<?>>asList(
                                new RealSubType<Operation>(log, listenersFactory, "operation", "Operation", "The operation to do", OperationType.ID, types))),
                new RealOption(log, listenersFactory, TRANSFORMATION_ID, TRANSFORMATION_NAME,
                        TRANSFORMATION_DESCRIPTION, Arrays.<RealSubType<?>>asList(
                                new RealSubType<Transformation>(log, listenersFactory, "path", "Path", "Path to the value", TransformationType.ID, types))));
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
        private final ListenersFactory listenersFactory;
        private final TypeSerialiser<RealObjectType.Reference<?>> realObjectTypeSerialiser;
        private final TypeSerialiser<Operation> operationSerialiser;
        private final TypeSerialiser<Transformation> transformationSerialiser;
        private final RootBridge root;
        private final RealList<TypeData<?>, RealType<?, ?, ?>> types;

        @Inject
        public Serialiser(Log log, ListenersFactory listenersFactory,
                          TypeSerialiser<RealObjectType.Reference<?>> realObjectTypeSerialiser,
                          TypeSerialiser<Operation> operationSerialiser, TypeSerialiser<Transformation> transformationSerialiser,
                          RootBridge root, RealList<TypeData<?>, RealType<?, ?, ?>> types) {
            this.log = log;
            this.listenersFactory = listenersFactory;
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
                typeInstance.getChildValues().getChildren().put(ConstantType.SUB_TYPE_ID, source.getValue().getTypeInstances());
                childValues.getChildren().put("type", new TypeInstances(typeInstance));
                return new TypeInstance(CONSTANT_ID, childValues);
            } else if(source instanceof ValueLocation) {
                TypeInstanceMap childValues = new TypeInstanceMap();
                childValues.getChildren().put("path", new TypeInstances(realObjectTypeSerialiser.serialise(((ValueLocation) source).getObjectReference())));
                return new TypeInstance(LOCATION_ID, childValues);
            } else if(source instanceof OperationOutput) {
                TypeInstanceMap childValues = new TypeInstanceMap();
                childValues.getChildren().put("operation", new TypeInstances(operationSerialiser.serialise(((OperationOutput)source).getOperation())));
                return new TypeInstance(OPERATION_ID, childValues);
            } else if(source instanceof TransformationOutput) {
                TypeInstanceMap childValues = new TypeInstanceMap();
                childValues.getChildren().put("transformation", new TypeInstances(transformationSerialiser.serialise(((TransformationOutput)source).getTransformation())));
                return new TypeInstance(TRANSFORMATION_ID, childValues);
            } else
                return null;
        }

        @Override
        public ValueSource deserialise(TypeInstance value) {
            if(value == null || value.getValue() == null)
                return null;
            if(value.getValue().equals(CONSTANT_ID)) {
                TypeInstances typeValue = value.getChildValues().getChildren().get("type");
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
                return new ConstantValue(listenersFactory, type, typeValue.getElements().get(0).getChildValues().getChildren().get(ConstantType.SUB_TYPE_ID));
            } else if(value.getValue().equals(LOCATION_ID)) {
                if(value.getChildValues().getChildren().get("path") != null && value.getChildValues().getChildren().get("path").getElements().size() > 0) {
                    RealObjectType.Reference<?> object = realObjectTypeSerialiser.deserialise(value.getChildValues().getChildren().get("path").getElements().get(0));
                    return new ValueLocation(listenersFactory, (RealObjectType.Reference<Value<?, ?>>)object, root);
                } else
                    return null;
            } else if(value.getValue().equals(OPERATION_ID)) {
                if(value.getChildValues().getChildren().get("operation") != null && value.getChildValues().getChildren().get("operation").getElements().size() > 0)
                    return new OperationOutput(log, listenersFactory, types, operationSerialiser.deserialise(value.getChildValues().getChildren().get("operation").getElements().get(0)));
                else
                    return null;
            } else if(value.getValue().equals(TRANSFORMATION_ID)) {
                if(value.getChildValues().getChildren().get("transformation") != null && value.getChildValues().getChildren().get("transformation").getElements().size() > 0)
                    return new TransformationOutput(log, listenersFactory, types, transformationSerialiser.deserialise(value.getChildValues().getChildren().get("transformation").getElements().get(0)));
                else
                    return null;
            } else
                return null;
        }
    }
}

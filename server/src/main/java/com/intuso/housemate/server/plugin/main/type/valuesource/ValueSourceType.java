package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealRoot;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.housemate.client.real.impl.internal.type.RealChoiceType;
import com.intuso.housemate.client.real.impl.internal.type.RealObjectType;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.plugin.main.type.constant.ConstantInstance;
import com.intuso.housemate.server.plugin.main.type.constant.ConstantType;
import com.intuso.housemate.server.plugin.main.type.operation.Operation;
import com.intuso.housemate.server.plugin.main.type.operation.OperationType;
import com.intuso.housemate.server.plugin.main.type.transformation.Transformation;
import com.intuso.housemate.server.plugin.main.type.transformation.TransformationType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 */
public class ValueSourceType extends RealChoiceType<ValueSource> {

    private final static Logger logger = LoggerFactory.getLogger(ValueSourceType.class);

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
    public ValueSourceType(ListenersFactory listenersFactory,
                           TypeSerialiser<ValueSource> serialiser,
                           RealRoot root) {
        super(logger, listenersFactory, ID, NAME, DESCRIPTION, 1, 1, createOptions(logger, listenersFactory, root.getTypes()));
        this.serialiser = serialiser;
    }

    private static List<RealOptionImpl> createOptions(Logger logger, ListenersFactory listenersFactory, RealList<RealType<?>> types) {
        return Arrays.asList(
                new RealOptionImpl(logger, listenersFactory, CONSTANT_ID, CONSTANT_NAME,
                        CONSTANT_DESCRIPTION,
                        new RealSubTypeImpl<ConstantInstance<Object>>(logger, listenersFactory, "type", "Type", "Type of the value", ConstantType.ID, types)),
                new RealOptionImpl(logger, listenersFactory, LOCATION_ID, LOCATION_NAME,
                        LOCATION_DESCRIPTION,
                        new RealSubTypeImpl<RealObjectType.Reference<Value<?, ?>>>(logger, listenersFactory, "path", "Path", "Path to the value", RealObjectType.ID, types)),
                new RealOptionImpl(logger, listenersFactory, OPERATION_ID, OPERATION_NAME,
                        OPERATION_DESCRIPTION,
                        new RealSubTypeImpl<Operation>(logger, listenersFactory, "operation", "Operation", "The operation to do", OperationType.ID, types)),
                new RealOptionImpl(logger, listenersFactory, TRANSFORMATION_ID, TRANSFORMATION_NAME,
                        TRANSFORMATION_DESCRIPTION,
                        new RealSubTypeImpl<Transformation>(logger, listenersFactory, "path", "Path", "Path to the value", TransformationType.ID, types)));
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

        private final ListenersFactory listenersFactory;
        private final TypeSerialiser<RealObjectType.Reference<?>> realObjectTypeSerialiser;
        private final TypeSerialiser<Operation> operationSerialiser;
        private final TypeSerialiser<Transformation> transformationSerialiser;
        private final RootBridge rootBridge;
        private final RealRoot root;

        @Inject
        public Serialiser(ListenersFactory listenersFactory,
                          TypeSerialiser<RealObjectType.Reference<?>> realObjectTypeSerialiser,
                          TypeSerialiser<Operation> operationSerialiser, TypeSerialiser<Transformation> transformationSerialiser,
                          RootBridge rootBridge,
                          RealRoot root) {
            this.listenersFactory = listenersFactory;
            this.realObjectTypeSerialiser = realObjectTypeSerialiser;
            this.operationSerialiser = operationSerialiser;
            this.transformationSerialiser = transformationSerialiser;
            this.rootBridge = rootBridge;
            this.root = root;
        }

        @Override
        public TypeInstance serialise(ValueSource source) {
            if(source instanceof ConstantValue) {
                TypeInstanceMap childValues = new TypeInstanceMap();
                TypeInstance typeInstance = new TypeInstance(source.getValue().getTypeId());
                typeInstance.getChildValues().getChildren().put(ConstantType.SUB_TYPE_ID, source.getValue().getValue());
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
                    logger.warn("Cannot deserialise constant, type id is null");
                    return null;
                }
                RealType<?> type = root.getTypes().get(typeId);
                if(type == null) {
                    logger.warn("Cannot deserialise constant, no type for id " + typeId);
                    return null;
                }
                return new ConstantValue(listenersFactory, type, typeValue.getElements().get(0).getChildValues().getChildren().get(ConstantType.SUB_TYPE_ID));
            } else if(value.getValue().equals(LOCATION_ID)) {
                if(value.getChildValues().getChildren().get("path") != null && value.getChildValues().getChildren().get("path").getElements().size() > 0) {
                    RealObjectType.Reference<?> object = realObjectTypeSerialiser.deserialise(value.getChildValues().getChildren().get("path").getElements().get(0));
                    return new ValueLocation(listenersFactory, (RealObjectType.Reference<Value<TypeInstances, ?>>)object, rootBridge);
                } else
                    return null;
            } else if(value.getValue().equals(OPERATION_ID)) {
                if(value.getChildValues().getChildren().get("operation") != null && value.getChildValues().getChildren().get("operation").getElements().size() > 0)
                    return new OperationOutput(logger, listenersFactory, root.getTypes(), operationSerialiser.deserialise(value.getChildValues().getChildren().get("operation").getElements().get(0)));
                else
                    return null;
            } else if(value.getValue().equals(TRANSFORMATION_ID)) {
                if(value.getChildValues().getChildren().get("transformation") != null && value.getChildValues().getChildren().get("transformation").getElements().size() > 0)
                    return new TransformationOutput(logger, listenersFactory, root.getTypes(), transformationSerialiser.deserialise(value.getChildValues().getChildren().get("transformation").getElements().get(0)));
                else
                    return null;
            } else
                return null;
        }
    }
}

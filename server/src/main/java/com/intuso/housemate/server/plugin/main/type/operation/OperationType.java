package com.intuso.housemate.server.plugin.main.type.operation;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.RealCompoundType;
import com.intuso.housemate.plugin.api.Operator;
import com.intuso.housemate.server.plugin.PluginListener;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSourceType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.Set;

/**
 */
public class OperationType extends RealCompoundType<Operation> {

    public final static String ID = "operation";
    public final static String NAME = "Operation";
    public final static String DESCRIPTION = "Operation of values";
    
    public final static String OPERATION_TYPE_ID = "operation-type";
    public final static String OPERATION_TYPE_NAME = "Operation Type";
    public final static String OPERATION_TYPE_DESCRIPTION = "The type of operation";
    public final static String VALUE_0_ID = "value0";
    public final static String VALUE_0_NAME = "First value";
    public final static String VALUE_0_DESCRIPTION = "The first value";
    public final static String VALUE_1_ID = "value1";
    public final static String VALUE_1_NAME = "Second value";
    public final static String VALUE_1_DESCRIPTION = "The second value";

    private final TypeSerialiser<Operation> serialiser;

    @Inject
    public OperationType(Log log, ListenersFactory listenersFactory,
                         RealList<TypeData<?>, RealType<?, ?, ?>> types, TypeSerialiser<Operation> serialiser) {
        super(log, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.serialiser = serialiser;
        getSubTypes().add(new RealSubType<com.intuso.housemate.plugin.api.OperationType>(log, listenersFactory,
                OPERATION_TYPE_ID, OPERATION_TYPE_NAME, OPERATION_TYPE_DESCRIPTION, OperationTypeType.ID, types));
        getSubTypes().add(new RealSubType<ValueSource>(log, listenersFactory, VALUE_0_ID, VALUE_0_NAME,
                VALUE_0_DESCRIPTION, ValueSourceType.ID, types));
        getSubTypes().add(new RealSubType<ValueSource>(log, listenersFactory, VALUE_1_ID, VALUE_1_NAME,
                VALUE_1_DESCRIPTION, ValueSourceType.ID, types));
    }

    @Override
    public TypeInstance serialise(Operation o) {
        return serialiser.serialise(o);
    }

    @Override
    public Operation deserialise(TypeInstance instance) {
        return serialiser.deserialise(instance);
    }

    public final static class Serialiser implements TypeSerialiser<Operation>, PluginListener {

        private final Log log;
        private final TypeSerialiser<com.intuso.housemate.plugin.api.OperationType> operationTypeSerialiser;
        private final TypeSerialiser<ValueSource> sourceTypeSerialiser;
        private final Map<com.intuso.housemate.plugin.api.OperationType, Map<String, Operator<?, ?>>> operators = Maps.newHashMap();

        @Inject
        public Serialiser(Log log,
                          PluginManager pluginManager,
                          TypeSerialiser<com.intuso.housemate.plugin.api.OperationType> operationTypeSerialiser,
                          TypeSerialiser<ValueSource> sourceTypeSerialiser) {
            this.log = log;
            this.operationTypeSerialiser = operationTypeSerialiser;
            this.sourceTypeSerialiser = sourceTypeSerialiser;
            pluginManager.addPluginListener(this, true);
        }

        @Override
        public TypeInstance serialise(Operation operationInstance) {
            if(operationInstance == null)
                return null;
            TypeInstance result = new TypeInstance();
            result.getChildValues().getChildren().put(OPERATION_TYPE_ID, new TypeInstances(operationTypeSerialiser.serialise(operationInstance.getOperationType())));
            result.getChildValues().getChildren().put(VALUE_0_ID, new TypeInstances(sourceTypeSerialiser.serialise(operationInstance.getFirstValueSource())));
            result.getChildValues().getChildren().put(VALUE_1_ID, new TypeInstances(sourceTypeSerialiser.serialise(operationInstance.getSecondValueSource())));
            return result;
        }

        @Override
        public Operation deserialise(TypeInstance instance) {
            if(instance == null
                    || instance.getChildValues().getChildren().get(OPERATION_TYPE_ID) == null
                    || instance.getChildValues().getChildren().get(OPERATION_TYPE_ID).getElements().size() == 0)
                return null;
            ValueSource value0 = null;
            if(instance.getChildValues().getChildren().get(VALUE_0_ID) != null && instance.getChildValues().getChildren().get(VALUE_0_ID).getElements().size() != 0)
                value0 = sourceTypeSerialiser.deserialise(instance.getChildValues().getChildren().get(VALUE_0_ID).getElements().get(0));
            ValueSource value1 = null;
            if(instance.getChildValues().getChildren().get(VALUE_1_ID) != null && instance.getChildValues().getChildren().get(VALUE_1_ID).getElements().size() != 0)
                value1 = sourceTypeSerialiser.deserialise(instance.getChildValues().getChildren().get(VALUE_1_ID).getElements().get(0));
            com.intuso.housemate.plugin.api.OperationType operationType = operationTypeSerialiser.deserialise(instance.getChildValues().getChildren().get(OPERATION_TYPE_ID).getElements().get(0));
            return new Operation(operationType, operators.get(operationType), value0, value1);
        }

        @Override
        public void pluginAdded(Injector pluginInjector) {
            for(Operator<?, ?> operator : pluginInjector.getInstance(new Key<Set<Operator<?, ?>>>() {})) {
                Map<String, Operator<?, ?>> operatorsByType = operators.get(operator.getOperationType());
                if(operatorsByType == null) {
                    operatorsByType = Maps.newHashMap();
                    operators.put(operator.getOperationType(), operatorsByType);
                }
                operatorsByType.put(operator.getInputTypeId(), operator);
            }
        }

        @Override
        public void pluginRemoved(Injector pluginInjector) {
            // todo remove them
        }
    }
}

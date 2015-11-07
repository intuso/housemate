package com.intuso.housemate.server.plugin.main.type.operation;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.housemate.client.real.impl.internal.type.RealCompoundType;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.housemate.plugin.api.internal.Operator;
import com.intuso.housemate.plugin.api.internal.PluginListener;
import com.intuso.housemate.plugin.api.internal.PluginResource;
import com.intuso.housemate.plugin.api.internal.TypeInfo;
import com.intuso.housemate.plugin.manager.PluginManager;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSourceType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 */
public class OperationType extends RealCompoundType<Operation> implements PluginListener {

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

    private final Log log;
    private final TypeSerialiser<TypeInfo> operationTypeSerialiser;
    private final TypeSerialiser<ValueSource> sourceTypeSerialiser;
    private final Map<String, Map<String, Operator<?, ?>>> operators = Maps.newHashMap();

    @Inject
    public OperationType(Log log,
                         ListenersFactory listenersFactory,
                         PluginManager pluginManager,
                         @com.intuso.housemate.server.plugin.main.ioc.Operator TypeSerialiser<TypeInfo> operationTypeSerialiser,
                         TypeSerialiser<ValueSource> sourceTypeSerialiser,
                         RealList<RealType<?>> types) {
        super(log, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.log = log;
        this.operationTypeSerialiser = operationTypeSerialiser;
        this.sourceTypeSerialiser = sourceTypeSerialiser;
        pluginManager.addPluginListener(this, true);
        getSubTypes().add(new RealSubTypeImpl<>(log, listenersFactory,
                OPERATION_TYPE_ID, OPERATION_TYPE_NAME, OPERATION_TYPE_DESCRIPTION, OperationTypeType.ID, types));
        getSubTypes().add(new RealSubTypeImpl<>(log, listenersFactory, VALUE_0_ID, VALUE_0_NAME,
                VALUE_0_DESCRIPTION, ValueSourceType.ID, types));
        getSubTypes().add(new RealSubTypeImpl<>(log, listenersFactory, VALUE_1_ID, VALUE_1_NAME,
                VALUE_1_DESCRIPTION, ValueSourceType.ID, types));
    }

    @Override
    public TypeInstance serialise(Operation operationInstance) {
        if(operationInstance == null)
            return null;
        TypeInstance result = new TypeInstance();
        result.getChildValues().getChildren().put(OPERATION_TYPE_ID, new TypeInstances(operationTypeSerialiser.serialise(operationInstance.getTypeInfo())));
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
        TypeInfo operationTypeInfo = operationTypeSerialiser.deserialise(instance.getChildValues().getChildren().get(OPERATION_TYPE_ID).getElements().get(0));
        return new Operation(operationTypeInfo, operators.get(operationTypeInfo), value0, value1);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        for(PluginResource<? extends Operator<?, ?>> operatorResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends Operator<?, ?>>>>() {})) {
            Map<String, Operator<?, ?>> operatorsByType = operators.get(operatorResource.getTypeInfo().id());
            if(operatorsByType == null) {
                operatorsByType = Maps.newHashMap();
                operators.put(operatorResource.getTypeInfo().id(), operatorsByType);
            }
            Operator<?, ?> operator = operatorResource.getResource();
            operatorsByType.put(operator.getInputTypeId(), operator);
        }
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        // todo remove them
    }
}

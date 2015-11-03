package com.intuso.housemate.server.plugin.main.type.operation;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.type.RealChoiceType;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.housemate.plugin.api.internal.OperationType;
import com.intuso.housemate.plugin.api.internal.Operator;
import com.intuso.housemate.plugin.api.internal.PluginListener;
import com.intuso.housemate.plugin.manager.PluginManager;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.Set;

/**
 */
public class OperationTypeType extends RealChoiceType<OperationType> implements PluginListener {

    public final static String ID = "operation-type";
    public final static String NAME = "Operation Type";
    public final static String DESCRIPTION = "Type of operation";

    private final ListenersFactory listenersFactory;

    private final TypeSerialiser<OperationType> serialiser;

    @Inject
    public OperationTypeType(Log log, ListenersFactory listenersFactory, TypeSerialiser<OperationType> serialiser, PluginManager pluginManager) {
        super(log, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.listenersFactory = listenersFactory;
        this.serialiser = serialiser;
        pluginManager.addPluginListener(this, true);
    }

    @Override
    public OperationType deserialise(TypeInstance instance) {
        return serialiser.deserialise(instance);
    }

    @Override
    public TypeInstance serialise(OperationType o) {
        return serialiser.serialise(o);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        for(Operator<?, ?> operator : pluginInjector.getInstance(new Key<Set<Operator<?, ?>>>() {}))
            if(getOptions().get(operator.getOperationType().getId()) == null) {
                RealOptionImpl option = new RealOptionImpl(getLog(), listenersFactory,
                        operator.getOperationType().getId(), operator.getOperationType().getName(), operator.getOperationType().getDescription());
                ((RealList<RealOptionImpl>)getOptions()).add(option);
            }
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        // todo remove them, not so easy as there might be many values for one key
    }

    public final static class Serialiser implements TypeSerialiser<OperationType>, PluginListener {

        private final Map<String, OperationType> types = Maps.newHashMap();

        @Inject
        public Serialiser(PluginManager pluginManager) {
            pluginManager.addPluginListener(this, true);
        }

        @Override
        public TypeInstance serialise(OperationType type) {
            return type != null ? new TypeInstance(type.getId()) : null;
        }

        @Override
        public OperationType deserialise(TypeInstance instance) {
            return instance != null ? types.get(instance.getValue()) : null;
        }

        @Override
        public void pluginAdded(Injector pluginInjector) {
            for(Operator<?, ?> operator : pluginInjector.getInstance(new Key<Set<Operator<?, ?>>>() {}))
                if(types.get(operator.getOperationType().getId()) == null)
                    types.put(operator.getOperationType().getId(), operator.getOperationType());
        }

        @Override
        public void pluginRemoved(Injector pluginInjector) {
            // todo remove them, not so easy as there might be many values for one key
        }
    }
}

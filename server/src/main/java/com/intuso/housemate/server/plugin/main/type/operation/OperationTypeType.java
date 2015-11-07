package com.intuso.housemate.server.plugin.main.type.operation;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.type.RealChoiceType;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.plugin.api.internal.Operator;
import com.intuso.housemate.plugin.api.internal.PluginListener;
import com.intuso.housemate.plugin.api.internal.PluginResource;
import com.intuso.housemate.plugin.api.internal.TypeInfo;
import com.intuso.housemate.plugin.manager.PluginManager;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 */
public class OperationTypeType extends RealChoiceType<TypeInfo> implements PluginListener {

    public final static String ID = "operation-type";
    public final static String NAME = "Operation Type";
    public final static String DESCRIPTION = "Type of operation";

    private final Map<String, TypeInfo> types = Maps.newHashMap();

    private final ListenersFactory listenersFactory;

    @Inject
    public OperationTypeType(Log log, ListenersFactory listenersFactory,
                             PluginManager pluginManager) {
        super(log, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.listenersFactory = listenersFactory;
        pluginManager.addPluginListener(this, true);
    }

    @Override
    public TypeInstance serialise(TypeInfo type) {
        return type != null ? new TypeInstance(type.id()) : null;
    }

    @Override
    public TypeInfo deserialise(TypeInstance instance) {
        return instance != null ? types.get(instance.getValue()) : null;
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        for(PluginResource<? extends Operator<?, ?>> operatorResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends Operator<?, ?>>>>() {})) {
            TypeInfo typeInfo = operatorResource.getTypeInfo();
            if (types.get(typeInfo.id()) == null) {
                types.put(typeInfo.id(), typeInfo);
                RealOptionImpl option = new RealOptionImpl(getLog(), listenersFactory,
                        typeInfo.id(), typeInfo.name(), typeInfo.description());
                ((RealList<RealOptionImpl>) getOptions()).add(option);
            }
        }
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        // todo remove them, not so easy as there might be many values for one key
    }
}

package com.intuso.housemate.server.plugin.main.type.comparison;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.type.RealChoiceType;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.plugin.api.internal.Comparator;
import com.intuso.housemate.plugin.api.internal.PluginListener;
import com.intuso.housemate.plugin.api.internal.PluginResource;
import com.intuso.housemate.plugin.manager.internal.PluginManager;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 */
public class ComparisonTypeType extends RealChoiceType<TypeInfo> implements PluginListener {

    private final static Logger logger = LoggerFactory.getLogger(ComparisonTypeType.class);

    public final static String ID = "comparison-type";
    public final static String NAME = "Comparison Type";
    public final static String DESCRIPTION = "Type for comparing values";

    private final Map<String, TypeInfo> types = Maps.newHashMap();

    private final ListenersFactory listenersFactory;

    @Inject
    public ComparisonTypeType(ListenersFactory listenersFactory,
                              PluginManager pluginManager) {
        super(logger, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
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
        for(PluginResource<? extends Comparator<?>> comparatorResource : pluginInjector.getInstance(new Key<Iterable<PluginResource<? extends Comparator<?>>>>() {})) {
            TypeInfo typeInfo = comparatorResource.getTypeInfo();
            if (types.get(typeInfo.id()) == null) {
                types.put(typeInfo.id(), typeInfo);
                RealOptionImpl option = new RealOptionImpl(getLogger(), listenersFactory,
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

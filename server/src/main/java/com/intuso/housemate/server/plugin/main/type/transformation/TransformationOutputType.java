package com.intuso.housemate.server.plugin.main.type.transformation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.type.RealChoiceType;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.plugin.api.internal.PluginListener;
import com.intuso.housemate.plugin.api.internal.Transformer;
import com.intuso.housemate.plugin.manager.internal.PluginManager;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class TransformationOutputType extends RealChoiceType<RealType<?>> implements PluginListener {

    public final static String ID = "output-type";
    public final static String NAME = "Output Type";
    public final static String DESCRIPTION = "The output type of the transformation";

    private final ListenersFactory listenersFactory;

    private final RealList<RealType<?>> types;

    @Inject
    public TransformationOutputType(final Logger logger,
                                    ListenersFactory listenersFactory,
                                    RealList<RealType<?>> types,
                                    PluginManager pluginManager) {
        super(logger, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.listenersFactory = listenersFactory;
        this.types = types;
        pluginManager.addPluginListener(this, true);
    }

    @Override
    public TypeInstance serialise(RealType<?> type) {
        return type != null ? new TypeInstance(type.getId()) : null;
    }

    @Override
    public RealType<?> deserialise(TypeInstance instance) {
        String typeId = instance != null ? instance.getValue() : null;
        return typeId != null ? types.get(typeId) : null;
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        for(Transformer<?, ?> transformer : pluginInjector.getInstance(new Key<Iterable<? extends Transformer<?, ?>>>() {}))
            if(getOptions().get(transformer.getOutputTypeId()) == null) {
                RealOptionImpl option = new RealOptionImpl(getLogger(), listenersFactory,
                        transformer.getOutputTypeId(), transformer.getOutputTypeId(), transformer.getOutputTypeId());
                ((RealList<RealOptionImpl>)getOptions()).add(option); // todo use the actual type's name and description here
            }
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        // todo remove them, not so easy as there might be many values for one key
    }
}

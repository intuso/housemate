package com.intuso.housemate.server.plugin.main.type.transformation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealOption;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.api.internal.impl.type.RealChoiceType;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.housemate.plugin.api.internal.PluginListener;
import com.intuso.housemate.plugin.api.internal.Transformer;
import com.intuso.housemate.plugin.manager.PluginManager;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Set;

/**
 */
public class TransformationOutputType extends RealChoiceType<RealType<?, ?, ?>> implements PluginListener {

    public final static String ID = "output-type";
    public final static String NAME = "Output Type";
    public final static String DESCRIPTION = "The output type of the transformation";

    private final ListenersFactory listenersFactory;

    private final TypeSerialiser<RealType<?, ?, ?>> serialiser;

    @Inject
    public TransformationOutputType(final Log log, ListenersFactory listenersFactory, TypeSerialiser<RealType<?, ?, ?>> serialiser, PluginManager pluginManager) {
        super(log, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.listenersFactory = listenersFactory;
        this.serialiser = serialiser;
        pluginManager.addPluginListener(this, true);
    }

    @Override
    public TypeInstance serialise(RealType<?, ?, ?> o) {
        return serialiser.serialise(o);
    }

    @Override
    public RealType<?, ?, ?> deserialise(TypeInstance instance) {
        return serialiser.deserialise(instance);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        for(Transformer<?, ?> transformer : pluginInjector.getInstance(new Key<Set<Transformer<?, ?>>>() {}))
            if(getOptions().getChild(transformer.getOutputTypeId()) == null)
                getOptions().add(new RealOption(getLog(), listenersFactory,
                        transformer.getOutputTypeId(), transformer.getOutputTypeId(), transformer.getOutputTypeId())); // todo use the actual type's name and description here
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        // todo remove them, not so easy as there might be many values for one key
    }

    public final static class Serialiser implements TypeSerialiser<RealType<?, ?, ?>> {

        private final RealList<TypeData<?>, RealType<?, ?, ?>> types;

        @Inject
        public Serialiser(RealList<TypeData<?>, RealType<?, ?, ?>> types) {
            this.types = types;
        }

        @Override
        public TypeInstance serialise(RealType<?, ?, ?> type) {
            return type != null ? new TypeInstance(type.getId()) : null;
        }

        @Override
        public RealType<?, ?, ?> deserialise(TypeInstance instance) {
            String typeId = instance != null ? instance.getValue() : null;
            return typeId != null ? types.get(typeId) : null;
        }
    }
}

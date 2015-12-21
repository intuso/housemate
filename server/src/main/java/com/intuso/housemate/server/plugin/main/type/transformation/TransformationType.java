package com.intuso.housemate.server.plugin.main.type.transformation;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.real.api.internal.RealRoot;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.housemate.client.real.impl.internal.type.RealCompoundType;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.housemate.plugin.api.internal.PluginListener;
import com.intuso.housemate.plugin.api.internal.Transformer;
import com.intuso.housemate.plugin.manager.internal.PluginManager;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSourceType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 */
public class TransformationType extends RealCompoundType<Transformation> implements PluginListener {

    private final static Logger logger = LoggerFactory.getLogger(TransformationType.class);

    public final static String ID = "transformation";
    public final static String NAME = "Transformation";
    public final static String DESCRIPTION = "Transformation of values";

    public final static String OUTPUT_TYPE_ID = "output-type";
    public final static String OUTPUT_TYPE_NAME = "Output Type";
    public final static String OUTPUT_TYPE_DESCRIPTION = "The output type of the transformation";
    
    public final static String VALUE_ID = "value";
    public final static String VALUE_NAME = "Value";
    public final static String VALUE_DESCRIPTION = "The value";

    private final TypeSerialiser<RealType<?>> outputTypeSerialiser;
    private final TypeSerialiser<ValueSource> sourceTypeSerialiser;
    private final Map<String, Map<String, Transformer<?, ?>>> transformers = Maps.newHashMap();

    @Inject
    public TransformationType(ListenersFactory listenersFactory,
                              PluginManager pluginManager,
                              TypeSerialiser<RealType<?>> outputTypeSerialiser,
                              TypeSerialiser<ValueSource> sourceTypeSerialiser,
                              RealRoot root) {
        super(logger, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.outputTypeSerialiser = outputTypeSerialiser;
        this.sourceTypeSerialiser = sourceTypeSerialiser;
        pluginManager.addPluginListener(this, true);
        getSubTypes().add(new RealSubTypeImpl<>(logger, listenersFactory, OUTPUT_TYPE_ID, OUTPUT_TYPE_NAME,
                OUTPUT_TYPE_DESCRIPTION, TransformationOutputType.ID, root.getTypes()));
        getSubTypes().add(new RealSubTypeImpl<>(logger, listenersFactory, VALUE_ID, VALUE_NAME,
                VALUE_DESCRIPTION, ValueSourceType.ID, root.getTypes()));
    }

    @Override
    public TypeInstance serialise(Transformation transformationInstance) {
        if(transformationInstance == null)
            return null;
        TypeInstance result = new TypeInstance();
        result.getChildValues().getChildren().put(OUTPUT_TYPE_ID, new TypeInstances(outputTypeSerialiser.serialise(transformationInstance.getOutputType())));
        result.getChildValues().getChildren().put(VALUE_ID, new TypeInstances(sourceTypeSerialiser.serialise(transformationInstance.getValueSource())));
        return result;
    }

    @Override
    public Transformation deserialise(TypeInstance instance) {
        RealType<?> outputType = null;
        if(instance.getChildValues().getChildren().get(OUTPUT_TYPE_ID) != null && instance.getChildValues().getChildren().get(OUTPUT_TYPE_ID).getElements().size() != 0)
            outputType = outputTypeSerialiser.deserialise(instance.getChildValues().getChildren().get(OUTPUT_TYPE_ID).getElements().get(0));
        ValueSource valueSource = null;
        if(instance.getChildValues().getChildren().get(VALUE_ID) != null && instance.getChildValues().getChildren().get(VALUE_ID).getElements().size() != 0)
            valueSource = sourceTypeSerialiser.deserialise(instance.getChildValues().getChildren().get(VALUE_ID).getElements().get(0));
        return new Transformation(outputType, transformers.get(outputType.getId()), valueSource);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        for(Transformer<?, ?> transformer : pluginInjector.getInstance(new Key<Iterable<? extends Transformer<?, ?>>>() {})) {
            Map<String, Transformer<?, ?>> transformersByType = transformers.get(transformer.getOutputTypeId());
            if(transformersByType == null) {
                transformersByType = Maps.newHashMap();
                transformers.put(transformer.getOutputTypeId(), transformersByType);
            }
            transformersByType.put(transformer.getInputTypeId(), transformer);
        }
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        // todo remove them
    }
}

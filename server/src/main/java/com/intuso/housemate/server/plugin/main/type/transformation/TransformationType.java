package com.intuso.housemate.server.plugin.main.type.transformation;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.RealCompoundType;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.housemate.plugin.api.Transformer;
import com.intuso.housemate.server.plugin.PluginListener;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSourceType;

import java.util.Map;

/**
 */
public class TransformationType extends RealCompoundType<Transformation> {

    public final static String ID = "transformation";
    public final static String NAME = "Transformation";
    public final static String DESCRIPTION = "Transformation of values";

    public final static String OUTPUT_TYPE_ID = "output-type";
    public final static String OUTPUT_TYPE_NAME = "Output Type";
    public final static String OUTPUT_TYPE_DESCRIPTION = "The output type of the transformation";
    
    public final static String VALUE_ID = "value";
    public final static String VALUE_NAME = "Value";
    public final static String VALUE_DESCRIPTION = "The value";

    private final TypeSerialiser<Transformation> serialiser;

    @Inject
    public TransformationType(final RealResources realResources,
                              TypeSerialiser<Transformation> serialiser,
                              RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        super(realResources, ID, NAME, DESCRIPTION, 1, 1);
        this.serialiser = serialiser;
        getSubTypes().add(new RealSubType<String>(realResources, OUTPUT_TYPE_ID, OUTPUT_TYPE_NAME, OUTPUT_TYPE_DESCRIPTION,
                TransformationOutputType.ID, types));
        getSubTypes().add(new RealSubType<ValueSource>(realResources, VALUE_ID, VALUE_NAME, VALUE_DESCRIPTION,
                ValueSourceType.ID, types));
    }

    @Override
    public TypeInstance serialise(Transformation o) {
        return serialiser.serialise(o);
    }

    @Override
    public Transformation deserialise(TypeInstance instance) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public final static class Serialiser implements TypeSerialiser<Transformation>, PluginListener {

        private final Map<String, Map<String, Transformer<?, ?>>> transformers = Maps.newHashMap();
        private final RealResources realResources;
        private final TypeSerialiser<String> outputTypeSerialiser;
        private final TypeSerialiser<ValueSource> sourceTypeSerialiser;

        @Inject
        public Serialiser(final RealResources realResources,
                                  PluginManager pluginManager,
                                  TypeSerialiser<String> outputTypeSerialiser,
                                  TypeSerialiser<ValueSource> sourceTypeSerialiser) {
            this.realResources = realResources;
            this.outputTypeSerialiser = outputTypeSerialiser;
            this.sourceTypeSerialiser = sourceTypeSerialiser;
            pluginManager.addPluginListener(this, true);
        }

        @Override
        public TypeInstance serialise(Transformation transformationInstance) {
            if(transformationInstance == null)
                return null;
            TypeInstance result = new TypeInstance();
            result.getChildValues().put(OUTPUT_TYPE_ID, new TypeInstances(outputTypeSerialiser.serialise(transformationInstance.getOutputType())));
            result.getChildValues().put(VALUE_ID, new TypeInstances(sourceTypeSerialiser.serialise(transformationInstance.getValueSource())));
            return result;
        }

        @Override
        public Transformation deserialise(TypeInstance instance) {
            String outputType = null;
            if(instance.getChildValues().get(OUTPUT_TYPE_ID) != null && instance.getChildValues().get(OUTPUT_TYPE_ID).size() != 0)
                outputType = outputTypeSerialiser.deserialise(instance.getChildValues().get(OUTPUT_TYPE_ID).get(0));
            ValueSource valueSource = null;
            if(instance.getChildValues().get(VALUE_ID) != null && instance.getChildValues().get(VALUE_ID).size() != 0)
                valueSource = sourceTypeSerialiser.deserialise(instance.getChildValues().get(VALUE_ID).get(0));
            return new Transformation(outputType, transformers.get(outputType), valueSource);
        }

        @Override
        public void pluginAdded(PluginDescriptor plugin) {
            for(Transformer<?, ?> transformer : plugin.getTransformers(realResources)) {
                Map<String, Transformer<?, ?>> transformersByType = transformers.get(transformer.getOutputTypeId());
                if(transformersByType == null) {
                    transformersByType = Maps.newHashMap();
                    transformers.put(transformer.getOutputTypeId(), transformersByType);
                }
                transformersByType.put(transformer.getInputTypeId(), transformer);
            }
        }

        @Override
        public void pluginRemoved(PluginDescriptor plugin) {
            // todo remove them
        }
    }
}

package com.intuso.housemate.broker.plugin.type.transformation;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.type.valuesource.ValueSource;
import com.intuso.housemate.broker.plugin.type.valuesource.ValueSourceType;
import com.intuso.housemate.object.broker.proxy.BrokerProxyType;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.object.real.impl.type.RealCompoundType;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.housemate.plugin.api.Transformer;

import java.util.Map;

/**
 */
public class TransformationType extends RealCompoundType<Transformation> implements PluginListener {

    public final static String ID = "transformation";
    public final static String NAME = "Transformation";
    public final static String DESCRIPTION = "Transformation of values";

    public final static String OUTPUT_TYPE_ID = "output-type";
    public final static String OUTPUT_TYPE_NAME = "Output Type";
    public final static String OUTPUT_TYPE_DESCRIPTION = "The output type of the transformation";
    
    public final static String VALUE_ID = "value";
    public final static String VALUE_NAME = "Value";
    public final static String VALUE_DESCRIPTION = "The value";

    private final RealChoiceType<String> outputTypeType;
    private final ValueSourceType sourceType;
    private final Map<String, Map<String, Transformer<?, ?>>> transformers = Maps.newHashMap();

    private final BrokerGeneralResources generalResources;

    public TransformationType(RealResources resources, BrokerGeneralResources generalResources) {
        this(resources, generalResources,
                (ValueSourceType) generalResources.getClient().getRoot().getTypes().get(ValueSourceType.ID));
    }

    public TransformationType(final RealResources resources, BrokerGeneralResources generalResources,
                              ValueSourceType sourceType) {
        super(resources, ID, NAME, DESCRIPTION, 1, 1);
        this.generalResources = generalResources;
        this.outputTypeType = new RealChoiceType<String>(resources, OUTPUT_TYPE_ID, OUTPUT_TYPE_NAME, OUTPUT_TYPE_DESCRIPTION, 1, 1) {
            @Override
            public TypeInstance serialise(String string) {
                return new TypeInstance(string);
            }

            @Override
            public String deserialise(TypeInstance instance) {
                return instance.getValue();
            }
        };
        generalResources.getProxyResources().getRoot().getTypes().addObjectListener(new ListListener<BrokerProxyType>() {
            @Override
            public void elementAdded(BrokerProxyType type) {
                outputTypeType.getOptions().add(new RealOption(resources, type.getId(), type.getName(), type.getDescription()));
            }

            @Override
            public void elementRemoved(BrokerProxyType element) {
                outputTypeType.getOptions().remove(element.getId());
            }
        });
        this.sourceType = sourceType;
        getSubTypes().add(new RealSubType<String>(resources, OUTPUT_TYPE_ID, OUTPUT_TYPE_NAME, OUTPUT_TYPE_DESCRIPTION,
                outputTypeType));
        getSubTypes().add(new RealSubType<ValueSource>(resources, VALUE_ID, VALUE_NAME, VALUE_DESCRIPTION,
                sourceType));
        generalResources.addPluginListener(this, true);
    }

    @Override
    public TypeInstance serialise(Transformation transformationInstance) {
        if(transformationInstance == null)
            return null;
        TypeInstance result = new TypeInstance();
        result.getChildValues().put(OUTPUT_TYPE_ID, new TypeInstances(outputTypeType.serialise(transformationInstance.getOutputType())));
        result.getChildValues().put(VALUE_ID, new TypeInstances(sourceType.serialise(transformationInstance.getValueSource())));
        return result;
    }

    @Override
    public Transformation deserialise(TypeInstance instance) {
        String outputType = null;
        if(instance.getChildValues().get(OUTPUT_TYPE_ID) != null && instance.getChildValues().get(OUTPUT_TYPE_ID).size() != 0)
            outputType = outputTypeType.deserialise(instance.getChildValues().get(OUTPUT_TYPE_ID).get(0));
        ValueSource valueSource = null;
        if(instance.getChildValues().get(VALUE_ID) != null && instance.getChildValues().get(VALUE_ID).size() != 0)
            valueSource = sourceType.deserialise(instance.getChildValues().get(VALUE_ID).get(0));
        return new Transformation(outputType, transformers.get(outputType), valueSource);
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(Transformer<?, ?> transformer : plugin.getTransformers(generalResources.getClientResources())) {
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

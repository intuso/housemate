package com.intuso.housemate.broker.plugin.type.valuesource;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.type.transformation.Transformation;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.Transformer;

/**
 */
public class TransformationOutput extends ValueSource implements ValueAvailableListener {

    private final BrokerGeneralResources generalResources;
    private final Transformation transformation;
    private ComputedValue value;

    public TransformationOutput(BrokerGeneralResources generalResources, Transformation transformation) {
        this.generalResources = generalResources;
        this.transformation = transformation;
        transformation.getValueSource().addValueAvailableListener(this);
    }

    public Transformation getTransformation() {
        return transformation;
    }

    private void transform() {

        Value inputValue = transformation.getValueSource().getValue();

        if(inputValue == null) {
            if(value != null) {
                value = null;
                for(ValueAvailableListener listener : listeners)
                    listener.valueUnavailable(this);
            }
            return;
        }

        RealType<?, ?, Object> inputType = (RealType<?, ?, Object>) generalResources.getClient().getRoot().getTypes().get(inputValue.getType().getId());
        Transformer<Object, Object> transformer = (Transformer<Object, Object>) transformation.getTransformersByType().get(inputType.getId());
        RealType<?, ?, Object> outputType = (RealType<?, ?, Object>) generalResources.getClient().getRoot().getTypes().get(transformer.getOutputTypeId());
        try {
            Object result = transformer.apply(inputType.deserialise(inputValue.getTypeInstances().get(0)));
            if(value != null && !value.getType().getId().equals(outputType.getId())) {
                for(ValueAvailableListener listener : listeners)
                    listener.valueUnavailable(this);
                value = null;
            }
            if(value == null)
                value = new ComputedValue(outputType);
            value.setTypeInstances(new TypeInstances(outputType.serialise(result)));
        } catch(HousemateException e) {
            generalResources.getLog().e("Failed to transform value");
            generalResources.getLog().st(e);
            value = null;
            return;
        }
    }

    @Override
    public Value<?, ?> getValue() {
        return value;
    }

    @Override
    public void valueAvailable(ValueSource source, Value<?, ?> value) {
        transform();
    }

    @Override
    public void valueUnavailable(ValueSource source) {
        if(value != null) {
            value = null;
            for(ValueAvailableListener listener : listeners)
                listener.valueUnavailable(this);
        }
    }
}

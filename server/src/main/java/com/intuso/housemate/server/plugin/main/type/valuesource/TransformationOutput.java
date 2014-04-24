package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.server.plugin.main.type.transformation.Transformation;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.Transformer;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class TransformationOutput extends ValueSource implements ValueAvailableListener {

    private final Log log;
    private final ListenersFactory listenersFactory;
    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final Transformation transformation;
    private ComputedValue value;

    public TransformationOutput(Log log, ListenersFactory listenersFactory, RealList<TypeData<?>, RealType<?, ?, ?>> types, Transformation transformation) {
        super(listenersFactory);
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.types = types;
        this.transformation = transformation;
        transformation.getValueSource().addValueAvailableListener(this, true);
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

        RealType<?, ?, Object> inputType = (RealType<?, ?, Object>) types.get(inputValue.getType().getId());
        Transformer<Object, Object> transformer = (Transformer<Object, Object>) transformation.getTransformersByType().get(inputType.getId());
        RealType<?, ?, Object> outputType = (RealType<?, ?, Object>) types.get(transformer.getOutputTypeId());
        try {
            Object result = transformer.apply(inputType.deserialise(inputValue.getTypeInstances().get(0)));
            if(value != null && !value.getType().getId().equals(outputType.getId())) {
                for(ValueAvailableListener listener : listeners)
                    listener.valueUnavailable(this);
                value = null;
            }
            if(value == null)
                value = new ComputedValue(listenersFactory, outputType);
            value.setTypeInstances(new TypeInstances(outputType.serialise(result)));
        } catch(HousemateException e) {
            log.e("Failed to transform value", e);
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

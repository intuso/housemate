package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.housemate.plugin.api.internal.Transformer;
import com.intuso.housemate.server.plugin.main.type.transformation.Transformation;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class TransformationOutput extends ValueSource implements ValueAvailableListener {

    private final Logger logger;
    private final ListenersFactory listenersFactory;
    private final RealList<RealType<?>> types;
    private final Transformation transformation;
    private ComputedValue value;

    public TransformationOutput(Logger logger, ListenersFactory listenersFactory, RealList<RealType<?>> types, Transformation transformation) {
        super(listenersFactory);
        this.logger = logger;
        this.listenersFactory = listenersFactory;
        this.types = types;
        this.transformation = transformation;
        transformation.getValueSource().addValueAvailableListener(this, true);
    }

    public Transformation getTransformation() {
        return transformation;
    }

    private void transform() {

        Value<TypeInstances, ?> inputValue = transformation.getValueSource().getValue();

        if(inputValue == null) {
            if(value != null) {
                value = null;
                for(ValueAvailableListener listener : listeners)
                    listener.valueUnavailable(this);
            }
            return;
        }

        RealType<Object> inputType = (RealType<Object>) types.get(inputValue.getTypeId());
        Transformer<Object, Object> transformer = (Transformer<Object, Object>) transformation.getTransformersByType().get(inputType.getId());
        RealType<Object> outputType = (RealType<Object>) types.get(transformer.getOutputTypeId());
        try {
            Object result = transformer.apply(inputType.deserialise(inputValue.getValue().getElements().get(0)));
            if(value != null && !value.getTypeId().equals(outputType.getId())) {
                for(ValueAvailableListener listener : listeners)
                    listener.valueUnavailable(this);
                value = null;
            }
            if(value == null)
                value = new ComputedValue(listenersFactory, outputType);
            value.setTypeInstances(new TypeInstances(outputType.serialise(result)));
        } catch(Throwable t) {
            logger.error("Failed to transform value", t);
            value = null;
            return;
        }
    }

    @Override
    public Value<TypeInstances, ?> getValue() {
        return value;
    }

    @Override
    public void valueAvailable(ValueSource source, Value<TypeInstances, ?> value) {
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

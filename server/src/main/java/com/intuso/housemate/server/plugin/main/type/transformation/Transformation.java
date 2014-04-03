package com.intuso.housemate.server.plugin.main.type.transformation;

import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.server.plugin.main.type.valuesource.ValueSource;
import com.intuso.housemate.plugin.api.Transformer;

import java.util.Map;

/**
 */
public class Transformation {

    private final RealType<?, ?, ?> outputType;
    private final Map<String, Transformer<?, ?>> transformersByType;
    private final ValueSource valueSource;

    public Transformation(RealType<?, ?, ?> outputType, Map<String, Transformer<?, ?>> transformersByType, ValueSource valueSource) {
        this.outputType = outputType;
        this.transformersByType = transformersByType;
        this.valueSource = valueSource;
    }

    public RealType<?, ?, ?> getOutputType() {
        return outputType;
    }

    public Map<String, Transformer<?, ?>> getTransformersByType() {
        return transformersByType;
    }

    public ValueSource getValueSource() {
        return valueSource;
    }
}

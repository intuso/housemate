package com.intuso.housemate.broker.plugin.type.transformation;

import com.intuso.housemate.broker.plugin.type.valuesource.ValueSource;
import com.intuso.housemate.plugin.api.Transformer;

import java.util.Map;

/**
 */
public class Transformation {

    private final String outputType;
    private final Map<String, Transformer<?, ?>> transformersByType;
    private final ValueSource valueSource;

    public Transformation(String outputType, Map<String, Transformer<?, ?>> transformersByType, ValueSource valueSource) {
        this.outputType = outputType;
        this.transformersByType = transformersByType;
        this.valueSource = valueSource;
    }

    public String getOutputType() {
        return outputType;
    }

    public Map<String, Transformer<?, ?>> getTransformersByType() {
        return transformersByType;
    }

    public ValueSource getValueSource() {
        return valueSource;
    }
}

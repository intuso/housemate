package com.intuso.housemate.plugin.api.bridge.v1_0;

import com.intuso.housemate.plugin.api.internal.Transformer;

/**
 * Created by tomc on 06/11/15.
 */
public class TransformerBridge<I, O> implements Transformer<I, O> {

    private final com.intuso.housemate.plugin.v1_0.api.Transformer<I, O> transformer;

    public TransformerBridge(com.intuso.housemate.plugin.v1_0.api.Transformer<I, O> transformer) {
        this.transformer = transformer;
    }

    @Override
    public String getInputTypeId() {
        return transformer.getInputTypeId();
    }

    @Override
    public String getOutputTypeId() {
        return transformer.getOutputTypeId();
    }

    @Override
    public O apply(I input) {
        return transformer.apply(input);
    }
}

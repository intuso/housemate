package com.intuso.housemate.plugin.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.plugin.v1_0.api.Transformer;

/**
 * Created by tomc on 06/11/15.
 */
public class TransformerMapper {

    private final Function<Transformer<?, ?>, com.intuso.housemate.plugin.api.internal.Transformer<?, ?>> fromV1_0Function = new Function<Transformer<?, ?>, com.intuso.housemate.plugin.api.internal.Transformer<?, ?>>() {
        @Override
        public com.intuso.housemate.plugin.api.internal.Transformer<?, ?> apply(Transformer<?, ?> operator) {
            return map(operator);
        }
    };

    public Function<Transformer<?, ?>, com.intuso.housemate.plugin.api.internal.Transformer<?, ?>> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public <I, O> com.intuso.housemate.plugin.api.internal.Transformer<I, O> map(Transformer<I, O> transformer) {
        if(transformer == null)
            return null;
        return new TransformerBridge<>(transformer);
    }
}

package com.intuso.housemate.plugin.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.plugin.v1_0.api.Operator;

/**
 * Created by tomc on 06/11/15.
 */
public class OperatorMapper {

    private final Function<Operator<?, ?>, com.intuso.housemate.plugin.api.internal.Operator<?, ?>> fromV1_0Function = new Function<Operator<?, ?>, com.intuso.housemate.plugin.api.internal.Operator<?, ?>>() {
        @Override
        public com.intuso.housemate.plugin.api.internal.Operator<?, ?> apply(Operator<?, ?> operator) {
            return map(operator);
        }
    };

    public Function<Operator<?, ?>, com.intuso.housemate.plugin.api.internal.Operator<?, ?>> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public <I, O> com.intuso.housemate.plugin.api.internal.Operator<I, O> map(Operator<I, O> operator) {
        if(operator == null)
            return null;
        return new OperatorBridge<>(operator);
    }
}

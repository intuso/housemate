package com.intuso.housemate.plugin.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.plugin.v1_0.api.Comparator;

/**
 * Created by tomc on 06/11/15.
 */
public class ComparatorMapper {

    private final Function<Comparator<?>, com.intuso.housemate.plugin.api.internal.Comparator<?>> fromV1_0Function = new Function<Comparator<?>, com.intuso.housemate.plugin.api.internal.Comparator<?>>() {
        @Override
        public com.intuso.housemate.plugin.api.internal.Comparator<?> apply(Comparator<?> operator) {
            return map(operator);
        }
    };

    public Function<Comparator<?>, com.intuso.housemate.plugin.api.internal.Comparator<?>> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public <O> com.intuso.housemate.plugin.api.internal.Comparator<O> map(Comparator<O> comparator) {
        if(comparator == null)
            return null;
        return new ComparatorBridge<>(comparator);
    }
}

package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.RealValue;

/**
 * Created by tomc on 03/11/15.
 */
public class ValueMapper {

    private final Function<com.intuso.housemate.client.real.api.internal.RealValue<?, ?, ?>, RealValue<?, ?, ?>> toV1_0Function = new Function<com.intuso.housemate.client.real.api.internal.RealValue<?, ?, ?>, RealValue<?, ?, ?>>() {
        @Override
        public RealValue<?, ?, ?> apply(com.intuso.housemate.client.real.api.internal.RealValue<?, ?, ?> value) {
            return map(value);
        }
    };

    private final Function<RealValue<?, ?, ?>, com.intuso.housemate.client.real.api.internal.RealValue<?, ?, ?>> fromV1_0Function = new Function<RealValue<?, ?, ?>, com.intuso.housemate.client.real.api.internal.RealValue<?, ?, ?>>() {
        @Override
        public com.intuso.housemate.client.real.api.internal.RealValue<?, ?, ?> apply(RealValue<?, ?, ?> value) {
            return map(value);
        }
    };

    private final RealValueBridge.Factory bridgeFactory;
    private final RealValueBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public ValueMapper(RealValueBridge.Factory bridgeFactory, RealValueBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public Function<com.intuso.housemate.client.real.api.internal.RealValue<?, ?, ?>, RealValue<?, ?, ?>> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<RealValue<?, ?, ?>, com.intuso.housemate.client.real.api.internal.RealValue<?, ?, ?>> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public <O> RealValue<O, ?, ?> map(com.intuso.housemate.client.real.api.internal.RealValue<O, ?, ?> value) {
        return map(value, new IdentityFunction<O>(), new IdentityFunction<O>());
    }

    public <FROM, TO> RealValue<TO, ?, ?> map(com.intuso.housemate.client.real.api.internal.RealValue<FROM, ?, ?> value, Function<? super FROM, ? extends TO> convertFrom, Function<? super TO, ? extends FROM> convertTo) {
        if(value == null)
            return null;
        if(value instanceof RealValueBridge)
            return ((RealValueBridge<TO, FROM>)value).getMappedValue();
        return (RealValue<TO, ?, ?>) reverseBridgeFactory.create(value, convertFrom, convertTo);
    }

    public <O> com.intuso.housemate.client.real.api.internal.RealValue<O, ?, ?> map(RealValue<O, ?, ?> value) {
        return map(value, new IdentityFunction<O>(), new IdentityFunction<O>());
    }

    public <FROM, TO> com.intuso.housemate.client.real.api.internal.RealValue<TO, ?, ?> map(RealValue<FROM, ?, ?> value, Function<? super FROM, ? extends TO> converterFrom, Function<? super TO, ? extends FROM> convertTo) {
        if(value == null)
            return null;
        if(value instanceof RealValueBridgeReverse)
            return ((RealValueBridgeReverse<TO, FROM>)value).getMappedValue();
        return (com.intuso.housemate.client.real.api.internal.RealValue<TO, ?, ?>) bridgeFactory.create(value, converterFrom, convertTo);
    }
}

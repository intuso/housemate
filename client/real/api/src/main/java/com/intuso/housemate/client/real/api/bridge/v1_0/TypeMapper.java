package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.object.RealType;

/**
 * Created by tomc on 06/11/15.
 */
public class TypeMapper {

    private final Function<com.intuso.housemate.client.real.api.internal.object.RealType<?, ?>, RealType<?, ?>> toV1_0Function = new Function<com.intuso.housemate.client.real.api.internal.object.RealType<?, ?>, RealType<?, ?>>() {
        @Override
        public RealType<?, ?> apply(com.intuso.housemate.client.real.api.internal.object.RealType<?, ?> type) {
            return map(type);
        }
    };

    private final Function<RealType<?, ?>, com.intuso.housemate.client.real.api.internal.object.RealType<?, ?>> fromV1_0Function = new Function<RealType<?, ?>, com.intuso.housemate.client.real.api.internal.object.RealType<?, ?>>() {
        @Override
        public com.intuso.housemate.client.real.api.internal.object.RealType<?, ?> apply(RealType<?, ?> type) {
            return map(type);
        }
    };

    private final RealTypeBridge.Factory bridgeFactory;
    private final RealTypeBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public TypeMapper(RealTypeBridge.Factory bridgeFactory, RealTypeBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public Function<RealType<?, ?>, com.intuso.housemate.client.real.api.internal.object.RealType<?, ?>> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public Function<com.intuso.housemate.client.real.api.internal.object.RealType<?, ?>, RealType<?, ?>> getToV1_0Function() {
        return toV1_0Function;
    }

    public <O> RealType<O, ?> map(com.intuso.housemate.client.real.api.internal.object.RealType<O, ?> value) {
        return map(value, new IdentityFunction<O>(), new IdentityFunction<O>());
    }

    public <FROM, TO> RealType<TO, ?> map(com.intuso.housemate.client.real.api.internal.object.RealType<FROM, ?> value, Function<? super FROM, ? extends TO> convertFrom, Function<? super TO, ? extends FROM> convertTo) {
        if(value == null)
            return null;
        if(value instanceof RealTypeBridge)
            return ((RealTypeBridge<TO, FROM>)value).getType();
        return (RealType<TO, ?>) reverseBridgeFactory.create(value, convertFrom, convertTo);
    }

    public <O> com.intuso.housemate.client.real.api.internal.object.RealType<O, ?> map(RealType<O, ?> value) {
        return map(value, new IdentityFunction<O>(), new IdentityFunction<O>());
    }

    public <FROM, TO> com.intuso.housemate.client.real.api.internal.object.RealType<TO, ?> map(RealType<FROM, ?> value, Function<? super FROM, ? extends TO> converterFrom, Function<? super TO, ? extends FROM> convertTo) {
        if(value == null)
            return null;
        if(value instanceof RealTypeBridgeReverse)
            return ((RealTypeBridgeReverse<TO, FROM>)value).getType();
        return (com.intuso.housemate.client.real.api.internal.object.RealType<TO, ?>) bridgeFactory.create(value, converterFrom, convertTo);
    }
}

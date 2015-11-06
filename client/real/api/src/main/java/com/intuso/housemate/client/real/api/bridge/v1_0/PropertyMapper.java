package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.RealProperty;

/**
 * Created by tomc on 03/11/15.
 */
public class PropertyMapper {

    private final Function<com.intuso.housemate.client.real.api.internal.RealProperty<?>, RealProperty<?>> toV1_0Function = new Function<com.intuso.housemate.client.real.api.internal.RealProperty<?>, RealProperty<?>>() {
        @Override
        public RealProperty<?> apply(com.intuso.housemate.client.real.api.internal.RealProperty<?> property) {
            return map(property);
        }
    };

    private final Function<RealProperty<?>, com.intuso.housemate.client.real.api.internal.RealProperty<?>> fromV1_0Function = new Function<RealProperty<?>, com.intuso.housemate.client.real.api.internal.RealProperty<?>>() {
        @Override
        public com.intuso.housemate.client.real.api.internal.RealProperty<?> apply(RealProperty<?> property) {
            return map(property);
        }
    };

    private final RealPropertyBridge.Factory bridgeFactory;
    private final RealPropertyBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public PropertyMapper(RealPropertyBridge.Factory bridgeFactory, RealPropertyBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public Function<com.intuso.housemate.client.real.api.internal.RealProperty<?>, RealProperty<?>> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<RealProperty<?>, com.intuso.housemate.client.real.api.internal.RealProperty<?>> getFromV1_0Function() {
        return fromV1_0Function;
    }
    
    public <O> RealProperty<O> map(com.intuso.housemate.client.real.api.internal.RealProperty<O> value) {
        return map(value, new IdentityFunction<O>(), new IdentityFunction<O>());
    }

    public <FROM, TO> RealProperty<TO> map(com.intuso.housemate.client.real.api.internal.RealProperty<FROM> property, Function<? super FROM, ? extends TO> convertFrom, Function<? super TO, ? extends FROM> convertTo) {
        if(property == null)
            return null;
        if(property instanceof RealPropertyBridge)
            return ((RealPropertyBridge<TO, FROM>)property).getProperty();
        return (RealProperty<TO>) reverseBridgeFactory.create(property, convertFrom, convertTo);
    }

    public <O> com.intuso.housemate.client.real.api.internal.RealProperty<O> map(RealProperty<O> value) {
        return map(value, new IdentityFunction<O>(), new IdentityFunction<O>());
    }

    public <FROM, TO> com.intuso.housemate.client.real.api.internal.RealProperty<TO> map(RealProperty<FROM> property, Function<? super FROM, ? extends TO> converterFrom, Function<? super TO, ? extends FROM> convertTo) {
        if(property == null)
            return null;
        if(property instanceof RealPropertyBridgeReverse)
            return ((RealPropertyBridgeReverse<TO, FROM>)property).getProperty();
        return (com.intuso.housemate.client.real.api.internal.RealProperty<TO>) bridgeFactory.create(property, converterFrom, convertTo);
    }
}

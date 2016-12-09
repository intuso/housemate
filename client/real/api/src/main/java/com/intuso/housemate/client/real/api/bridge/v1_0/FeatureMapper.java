package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.object.RealFeature;

/**
 * Created by tomc on 03/11/15.
 */
public class FeatureMapper {

    private final Function<com.intuso.housemate.client.real.api.internal.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, RealFeature<?, ?, ?, ?, ?, ?, ?, ?>> toV1_0Function = new Function<com.intuso.housemate.client.real.api.internal.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, RealFeature<?, ?, ?, ?, ?, ?, ?, ?>>() {
        @Override
        public RealFeature<?, ?, ?, ?, ?, ?, ?, ?> apply(com.intuso.housemate.client.real.api.internal.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?> feature) {
            return map(feature);
        }
    };

    private final Function<RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, com.intuso.housemate.client.real.api.internal.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?>> fromV1_0Function = new Function<RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, com.intuso.housemate.client.real.api.internal.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?>>() {
        @Override
        public com.intuso.housemate.client.real.api.internal.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?> apply(RealFeature<?, ?, ?, ?, ?, ?, ?, ?> feature) {
            return map(feature);
        }
    };

    private final RealFeatureBridge.Factory bridgeFactory;
    private final RealFeatureBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public FeatureMapper(RealFeatureBridge.Factory bridgeFactory, RealFeatureBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public Function<com.intuso.housemate.client.real.api.internal.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, RealFeature<?, ?, ?, ?, ?, ?, ?, ?>> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, com.intuso.housemate.client.real.api.internal.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?>> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public RealFeature<?, ?, ?, ?, ?, ?, ?, ?> map(com.intuso.housemate.client.real.api.internal.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?> feature) {
        if(feature == null)
            return null;
        if(feature instanceof RealFeatureBridge)
            return ((RealFeatureBridge)feature).getFeature();
        return reverseBridgeFactory.create(feature);
    }

    public com.intuso.housemate.client.real.api.internal.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?> map(RealFeature<?, ?, ?, ?, ?, ?, ?, ?> feature) {
        if(feature == null)
            return null;
        if(feature instanceof RealFeatureBridgeReverse)
            return ((RealFeatureBridgeReverse)feature).getFeature();
        return bridgeFactory.create(feature);
    }
}

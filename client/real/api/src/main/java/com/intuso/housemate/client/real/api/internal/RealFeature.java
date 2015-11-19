package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.comms.api.internal.payload.FeatureData;
import com.intuso.housemate.object.api.internal.Feature;

/**
 * Base class for all devices
 */
public interface RealFeature extends Feature<
        RealList<RealCommand>,
        RealList<RealValue<?>>,
        RealFeature> {

    interface Factory {
            RealFeature create(FeatureData data);
    }
}

package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.object.api.internal.Feature;

/**
 * Base class for all devices
 */
public interface RealFeature extends Feature<
        RealList<RealCommand>,
        RealList<RealValue<?>>,
        RealFeature> {}

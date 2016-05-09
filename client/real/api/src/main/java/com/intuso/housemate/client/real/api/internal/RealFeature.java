package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Feature;

/**
 * Base class for all devices
 */
public interface RealFeature<COMMANDS extends com.intuso.housemate.client.real.api.internal.RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealValue<?, ?, ?>, ?>,
        FEATURE extends RealFeature<COMMANDS, VALUES, FEATURE>>
        extends Feature<COMMANDS,
                VALUES,
                FEATURE> {}

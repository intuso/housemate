package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Feature;

/**
 * Base class for all devices
 */
public interface RealFeature<COMMANDS extends RealList<? extends RealCommand<?, ?, ?>, ?>,
        VALUES extends RealList<? extends RealValue<?, ?, ?>, ?>,
        FEATURE extends RealFeature<COMMANDS, VALUES, FEATURE>>
        extends Feature<COMMANDS,
        VALUES,
        FEATURE> {}

package com.intuso.housemate.client.real.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Option;

public interface RealOption<SUB_TYPES extends RealList<? extends RealSubType<?, ?, ?>, ?>,
        OPTION extends RealOption<SUB_TYPES, OPTION>>
        extends Option<SUB_TYPES,
        OPTION> {}

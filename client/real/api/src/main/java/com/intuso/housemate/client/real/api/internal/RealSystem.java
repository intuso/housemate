package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.System;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.proxy.internal.simple.SimpleProxyDevice;

/**
 * Base class for all real systems
 */
public interface RealSystem<
        STRING_VALUE extends RealValue<String, ?, ?>,
        COMMAND extends RealCommand<?, ?, ?>,
        PROPERTIES extends RealList<? extends RealProperty<ObjectReference<SimpleProxyDevice>, ?, ?, ?>, ?>,
        SYSTEM extends RealSystem<STRING_VALUE, COMMAND, PROPERTIES, SYSTEM>>
        extends System<STRING_VALUE, COMMAND, PROPERTIES, SYSTEM> {}

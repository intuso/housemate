package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.System;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.proxy.internal.simple.SimpleProxyConnectedDevice;

/**
 * Base class for all real systems
 */
public interface RealSystem<
        STRING_VALUE extends RealValue<String, ?, ?>,
        COMMAND extends RealCommand<?, ?, ?>,
        PROPERTIES extends RealList<? extends RealProperty<ObjectReference<SimpleProxyConnectedDevice>, ?, ?, ?>, ?>,
        SYSTEM extends RealSystem<STRING_VALUE, COMMAND, PROPERTIES, SYSTEM>>
        extends System<STRING_VALUE, COMMAND, PROPERTIES, SYSTEM> {

    interface Container<SYSTEM extends RealSystem<?, ?, ?, ?>, SYSTEMS extends RealList<? extends SYSTEM, ?>> extends System.Container<SYSTEMS>, RemoveCallback<SYSTEM> {
        void addSystem(SYSTEM system);
    }

    interface RemoveCallback<SYSTEM extends RealSystem<?, ?, ?, ?>> {
        void removeSystem(SYSTEM system);
    }
}

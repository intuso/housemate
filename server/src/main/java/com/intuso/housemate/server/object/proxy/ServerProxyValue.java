package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.ValueData;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

public class ServerProxyValue extends ServerProxyValueBase<ValueData, NoChildrenData,
            NoChildrenServerProxyObject, Value.Listener<? super ServerProxyValue>, ServerProxyValue>
        implements Value<TypeInstances, ServerProxyValue> {
    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyValue(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted ValueData data) {
        super(log, listenersFactory, objectFactory, data);
    }
}

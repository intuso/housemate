package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.ValueData;
import com.intuso.housemate.comms.v1_0.api.ObjectFactory;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

public class ServerProxyValue extends ServerProxyValueBase<ValueData, NoChildrenData,
            NoChildrenServerProxyObject, Value.Listener<? super ServerProxyValue>, ServerProxyValue>
        implements Value<TypeInstances, ServerProxyValue> {
    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyValue(ListenersFactory listenersFactory,
                            ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory,
                            @Assisted Logger logger,
                            @Assisted ValueData data) {
        super(listenersFactory, objectFactory, logger, data);
    }
}

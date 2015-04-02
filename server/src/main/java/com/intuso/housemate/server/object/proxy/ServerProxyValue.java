package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerProxyValue extends ServerProxyValueBase<ValueData, NoChildrenData,
            NoChildrenServerProxyObject, ServerProxyValue> {
    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyValue(Log log, ListenersFactory listenersFactory, Injector injector, @Assisted ValueData data) {
        super(log, listenersFactory, injector, data);
    }
}

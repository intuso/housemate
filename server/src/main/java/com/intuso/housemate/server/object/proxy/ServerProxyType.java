package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.housemate.object.api.internal.Type;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

public class ServerProxyType
        extends ServerProxyObject<TypeData<HousemateData<?>>,
                HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyType, Type.Listener<? super ServerProxyType>>
        implements Type<ServerProxyType> {
    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyType(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted TypeData<HousemateData<?>> data) {
        super(log, listenersFactory, objectFactory, data);
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // do nothing
    }
}

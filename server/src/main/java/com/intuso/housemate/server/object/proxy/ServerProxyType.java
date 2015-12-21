package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.housemate.comms.v1_0.api.ObjectFactory;
import com.intuso.housemate.object.api.internal.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

public class ServerProxyType
        extends ServerProxyObject<TypeData<HousemateData<?>>,
                HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyType, Type.Listener<? super ServerProxyType>>
        implements Type<ServerProxyType> {
    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyType(ListenersFactory listenersFactory,
                           ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory,
                           @Assisted Logger logger,
                           @Assisted TypeData<HousemateData<?>> data) {
        super(listenersFactory, objectFactory, logger, data);
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // do nothing
    }
}

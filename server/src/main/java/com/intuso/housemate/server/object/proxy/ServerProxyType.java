package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.housemate.object.api.internal.Type;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.object.ObjectFactory;
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
    public ServerProxyType(Logger logger, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted TypeData<HousemateData<?>> data) {
        super(logger, listenersFactory, objectFactory, data);
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // do nothing
    }
}

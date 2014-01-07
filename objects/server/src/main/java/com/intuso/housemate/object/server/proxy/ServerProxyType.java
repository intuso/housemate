package com.intuso.housemate.object.server.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.utilities.log.Log;

public class ServerProxyType
        extends ServerProxyObject<TypeData<HousemateData<?>>,
                HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyType, TypeListener>
        implements Type {
    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyType(Log log, Injector injector, @Assisted TypeData<HousemateData<?>> data) {
        super(log, injector, data);
    }
}

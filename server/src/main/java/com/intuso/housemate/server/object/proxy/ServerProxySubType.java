package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.SubTypeData;
import com.intuso.housemate.object.api.internal.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

public class ServerProxySubType
        extends ServerProxyObject<SubTypeData, NoChildrenData, NoChildrenServerProxyObject, ServerProxySubType, SubType.Listener<? super ServerProxySubType>>
        implements SubType<ServerProxySubType> {

    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxySubType(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory,
                              @Assisted SubTypeData data) {
        super(log, listenersFactory, objectFactory, data);
    }

    @Override
    public void getChildObjects() {
        super.getChildObjects();
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // do nothing
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }
}

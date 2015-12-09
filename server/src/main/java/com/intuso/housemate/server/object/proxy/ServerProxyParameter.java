package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.ParameterData;
import com.intuso.housemate.object.api.internal.Parameter;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.object.ObjectFactory;
import org.slf4j.Logger;

public class ServerProxyParameter
        extends ServerProxyObject<ParameterData, NoChildrenData, NoChildrenServerProxyObject, ServerProxyParameter, Parameter.Listener<? super ServerProxyParameter>>
        implements Parameter<ServerProxyParameter> {

    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyParameter(Logger logger, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory,
                                @Assisted ParameterData data) {
        super(logger, listenersFactory, objectFactory, data);
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

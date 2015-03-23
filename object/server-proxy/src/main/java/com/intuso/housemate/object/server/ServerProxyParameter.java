package com.intuso.housemate.object.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerProxyParameter
        extends ServerProxyObject<ParameterData, NoChildrenData, NoChildrenServerProxyObject, ServerProxyParameter, ParameterListener>
        implements Parameter<ServerProxyType> {

    private final ServerProxyList<TypeData<?>, ServerProxyType> types;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyParameter(Log log, ListenersFactory listenersFactory, Injector injector, ServerProxyList<TypeData<?>, ServerProxyType> types,
                                @Assisted ParameterData data) {
        super(log, listenersFactory, injector, data);
        this.types = types;
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

    @Override
    public final ServerProxyType getType() {
        return types.get(getData().getType());
    }
}

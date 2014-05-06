package com.intuso.housemate.object.server.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerProxySubType
        extends ServerProxyObject<SubTypeData, NoChildrenData, NoChildrenServerProxyObject, ServerProxySubType, SubTypeListener>
        implements SubType<ServerProxyType> {

    private ServerProxyList<TypeData<?>, ServerProxyType> types;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxySubType(Log log, ListenersFactory listenersFactory, Injector injector, ServerProxyList<TypeData<?>, ServerProxyType> types,
                              @Assisted SubTypeData data) {
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

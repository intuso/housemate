package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HardwareData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.object.api.internal.Hardware;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

public class ServerProxyHardware
        extends ServerProxyObject<
        HardwareData,
        HousemateData<?>,
            ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyHardware,
            Hardware.Listener<? super ServerProxyHardware>>
        implements Hardware<
            ServerProxyList<PropertyData, ServerProxyProperty>,
            ServerProxyHardware> {

    private ServerProxyList<PropertyData, ServerProxyProperty> properties;

    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyHardware(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted HardwareData data) {
        super(log, listenersFactory, objectFactory, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        properties = (ServerProxyList<PropertyData, ServerProxyProperty>) getChild(HardwareData.PROPERTIES_ID);
    }

    @Override
    public ServerProxyList<PropertyData, ServerProxyProperty> getProperties() {
        return properties;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof HardwareData)
            getProperties().copyValues(data.getChildData(HardwareData.PROPERTIES_ID));
    }
}

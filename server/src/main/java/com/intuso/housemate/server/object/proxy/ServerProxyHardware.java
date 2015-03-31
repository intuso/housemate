package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.hardware.Hardware;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.hardware.HardwareListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerProxyHardware
        extends ServerProxyObject<
            HardwareData,
            HousemateData<?>,
            ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyHardware,
            HardwareListener<? super ServerProxyHardware>>
        implements Hardware<
            ServerProxyList<PropertyData, ServerProxyProperty>,
            ServerProxyHardware> {

    private ServerProxyList<PropertyData, ServerProxyProperty> properties;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyHardware(Log log, ListenersFactory listenersFactory, Injector injector, @Assisted HardwareData data) {
        super(log, listenersFactory, injector, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        properties = (ServerProxyList<PropertyData, ServerProxyProperty>) getChild(PROPERTIES_ID);
    }

    @Override
    public ServerProxyList<PropertyData, ServerProxyProperty> getProperties() {
        return properties;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof HardwareData)
            getProperties().copyValues(data.getChildData(PROPERTIES_ID));
    }
}

package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.hardware.Hardware;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.hardware.HardwareListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/*
 * @param <PROPERTIES> the type of the properties list
 * @param <HARDWARE> the type of the hardware
 */
public abstract class ProxyHardware<
            PROPERTIES extends ProxyList<PropertyData, ? extends ProxyProperty<?, ?, ?>, PROPERTIES>,
            HARDWARE extends ProxyHardware<PROPERTIES, HARDWARE>>
        extends ProxyObject<HardwareData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, HARDWARE, HardwareListener<? super HARDWARE>>
        implements Hardware<PROPERTIES, HARDWARE> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyHardware(Log log, ListenersFactory listenersFactory, HardwareData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public final PROPERTIES getProperties() {
        return (PROPERTIES) getChild(PROPERTIES_ID);
    }
}

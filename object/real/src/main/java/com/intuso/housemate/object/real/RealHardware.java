package com.intuso.housemate.object.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.hardware.Hardware;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.hardware.HardwareListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Base class for all devices
 */
public class RealHardware
        extends RealObject<HardwareData, HousemateData<?>, RealObject<?, ?, ?, ?>, HardwareListener<? super RealHardware>>
        implements Hardware<
            RealList<PropertyData, RealProperty<?>>,
            RealHardware> {

    private final static String PROPERTIES_DESCRIPTION = "The hardware's properties";

    private RealList<PropertyData, RealProperty<?>> properties;

    public RealHardware(Log log, ListenersFactory listenersFactory, String id, String name, String description, RealProperty<?> ... properties) {
        this(log, listenersFactory, id, name, description, Lists.newArrayList(properties));
    }

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the hardware's id
     * @param name the hardware's name
     * @param description the hardware's description
     */
    public RealHardware(Log log, ListenersFactory listenersFactory, String id, String name, String description, List<RealProperty<?>> properties) {
        super(log, listenersFactory, new HardwareData(id, name, description));
        this.properties = new RealList<PropertyData, RealProperty<?>>(log, listenersFactory, PROPERTIES_ID, PROPERTIES_ID, PROPERTIES_DESCRIPTION, properties);
        addChild(this.properties);
    }

    @Override
    public final RealList<PropertyData, RealProperty<?>> getProperties() {
        return properties;
    }
}

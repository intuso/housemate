package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.hardware.Hardware;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.hardware.HardwareListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.ServerProxyType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class HardwareBridge
        extends BridgeObject<
            HardwareData,
            HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>,
            HardwareBridge,
            HardwareListener<? super HardwareBridge>>
        implements Hardware<
            ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>,
            HardwareBridge> {
private ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> propertyList;

    public HardwareBridge(Log log, ListenersFactory listenersFactory, Hardware<?, ?> hardware,
                          ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, listenersFactory, new HardwareData(hardware.getId(), hardware.getName(), hardware.getDescription()));
        propertyList = new SingleListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>(log, listenersFactory, hardware.getProperties(),
                new PropertyBridge.Converter(log, listenersFactory, types));
        addChild(propertyList);
    }

    @Override
    public ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    public static class Converter implements Function<Hardware<?, ?>, HardwareBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(Log log, ListenersFactory listenersFactory, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.log = log;
            this.listenersFactory = listenersFactory;
            this.types = types;;
        }

        @Override
        public HardwareBridge apply(Hardware<?, ?> hardware) {
            return new HardwareBridge(log, listenersFactory, hardware, types);
        }
    }
}

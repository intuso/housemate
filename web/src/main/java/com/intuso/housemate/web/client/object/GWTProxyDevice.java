package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.ProxyDevice;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyDevice
        extends ProxyDevice<
            GWTProxyCommand,
            GWTProxyList<CommandData, GWTProxyCommand>,
            GWTProxyValue,
            GWTProxyList<ValueData, GWTProxyValue>,
            GWTProxyProperty,
            GWTProxyList<PropertyData, GWTProxyProperty>,
        GWTProxyFeature,
            GWTProxyDevice> {
    public GWTProxyDevice(Log log,
                          Injector injector,
                          @Assisted DeviceData data) {
        super(log, injector, data);
    }
}

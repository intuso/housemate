package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.ProxyDevice;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;

/**
 */
public class GWTProxyDevice
        extends ProxyDevice<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            GWTProxyCommand,
            GWTProxyList<CommandData, GWTProxyCommand>,
            GWTProxyValue,
            GWTProxyList<ValueData, GWTProxyValue>,
            GWTProxyProperty,
            GWTProxyList<PropertyData, GWTProxyProperty>,
        GWTProxyFeature,
            GWTProxyDevice> {
    public GWTProxyDevice(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                          GWTResources<?> childResources,
                          DeviceData data) {
        super(resources, childResources, data);
    }
}

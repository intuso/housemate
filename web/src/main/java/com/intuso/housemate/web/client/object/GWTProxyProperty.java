package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.object.proxy.ProxyProperty;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyProperty extends ProxyProperty<GWTResources<GWTProxyFactory.Command>, GWTResources<?>, GWTProxyType, GWTProxyCommand, GWTProxyProperty> {
    public GWTProxyProperty(GWTResources<GWTProxyFactory.Command> resources, GWTResources<?> childResources, PropertyData data) {
        super(resources, childResources, data);
    }
}

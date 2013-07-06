package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyType;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyType extends ProxyType<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
        TypeData<HousemateData<?>>,
        HousemateData<?>,
            ProxyObject<?, ?, ?, ?, ?, ?, ?>,
            GWTProxyType> {
    public GWTProxyType(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                        GWTResources<?> childResources,
                        TypeData data) {
        super(resources, childResources, data);
    }
}

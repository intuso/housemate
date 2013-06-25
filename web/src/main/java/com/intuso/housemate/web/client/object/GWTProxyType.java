package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyType;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyType extends ProxyType<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            TypeWrappable<HousemateObjectWrappable<?>>,
            HousemateObjectWrappable<?>,
            ProxyObject<?, ?, ?, ?, ?, ?, ?>,
            GWTProxyType> {
    public GWTProxyType(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                        GWTResources<?> childResources,
                        TypeWrappable wrappable) {
        super(resources, childResources, wrappable);
    }
}

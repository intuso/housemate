package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.subtype.SubTypeWrappable;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxySubType;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxySubType extends ProxySubType<GWTResources<NoChildrenProxyObjectFactory>, GWTProxyType,
        GWTProxySubType> {
    public GWTProxySubType(GWTResources<NoChildrenProxyObjectFactory> resources, SubTypeWrappable data) {
        super(resources, data);
    }
}

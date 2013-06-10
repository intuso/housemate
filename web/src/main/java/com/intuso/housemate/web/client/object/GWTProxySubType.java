package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.subtype.SubTypeWrappable;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxySubType;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 09/02/13
 * Time: 08:45
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxySubType extends ProxySubType<GWTResources<NoChildrenProxyObjectFactory>, GWTProxyType,
        GWTProxySubType> {
    public GWTProxySubType(GWTResources<NoChildrenProxyObjectFactory> resources, SubTypeWrappable wrappable) {
        super(resources, wrappable);
    }
}

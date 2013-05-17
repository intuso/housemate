package com.intuso.housemate.web.client.object;

import com.intuso.housemate.core.object.value.ValueWrappable;
import com.intuso.housemate.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.proxy.ProxyValue;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/08/12
 * Time: 00:03
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyValue extends ProxyValue<GWTResources<NoChildrenProxyObjectFactory>, GWTProxyType, GWTProxyValue> {
    public GWTProxyValue(GWTResources<NoChildrenProxyObjectFactory> resources, ValueWrappable value) {
        super(resources, value);
    }
}

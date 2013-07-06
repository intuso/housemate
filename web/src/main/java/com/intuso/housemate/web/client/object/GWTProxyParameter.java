package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxyParameter;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyParameter extends ProxyParameter<GWTResources<NoChildrenProxyObjectFactory>, GWTProxyType,
        GWTProxyParameter> {
    public GWTProxyParameter(GWTResources<NoChildrenProxyObjectFactory> resources, ParameterData data) {
        super(resources, data);
    }
}

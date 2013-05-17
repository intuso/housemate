package com.intuso.housemate.web.client.object;

import com.intuso.housemate.core.object.command.argument.ArgumentWrappable;
import com.intuso.housemate.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.proxy.ProxyArgument;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 09/02/13
 * Time: 08:45
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyArgument extends ProxyArgument<GWTResources<NoChildrenProxyObjectFactory>, GWTProxyType,
            GWTProxyArgument> {
    public GWTProxyArgument(GWTResources<NoChildrenProxyObjectFactory> resources, ArgumentWrappable wrappable) {
        super(resources, wrappable);
    }
}

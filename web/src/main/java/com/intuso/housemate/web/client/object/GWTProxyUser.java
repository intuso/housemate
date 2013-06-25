package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.object.proxy.ProxyUser;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyUser extends ProxyUser<GWTResources<GWTProxyFactory.All>, GWTResources<?>, GWTProxyCommand,
        GWTProxyUser> {
    public GWTProxyUser(GWTResources<GWTProxyFactory.All> resources, GWTResources<?> childResources, UserWrappable wrappable) {
        super(resources, childResources, wrappable);
    }
}

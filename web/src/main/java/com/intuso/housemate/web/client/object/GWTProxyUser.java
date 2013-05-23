package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.object.proxy.ProxyUser;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/08/12
 * Time: 00:04
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyUser extends ProxyUser<GWTResources<GWTProxyFactory.All>, GWTResources<?>, GWTProxyCommand,
        GWTProxyUser> {
    public GWTProxyUser(GWTResources<GWTProxyFactory.All> resources, GWTResources<?> subResources, UserWrappable wrappable) {
        super(resources, subResources, wrappable);
    }
}

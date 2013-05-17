package com.intuso.housemate.web.client.object;

import com.intuso.housemate.core.object.property.PropertyWrappable;
import com.intuso.housemate.proxy.ProxyProperty;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/03/12
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyProperty extends ProxyProperty<GWTResources<GWTProxyFactory.Command>, GWTResources<?>, GWTProxyType, GWTProxyCommand, GWTProxyProperty> {
    public GWTProxyProperty(GWTResources<GWTProxyFactory.Command> resources, GWTResources<?> subResources, PropertyWrappable wrappable) {
        super(resources, subResources, wrappable);
    }
}

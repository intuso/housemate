package com.intuso.housemate.web.client.object;

import com.intuso.housemate.core.object.HousemateObjectFactory;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.type.TypeWrappable;
import com.intuso.housemate.core.object.device.DeviceWrappable;
import com.intuso.housemate.core.object.rule.RuleWrappable;
import com.intuso.housemate.core.object.user.UserWrappable;
import com.intuso.housemate.proxy.ProxyObject;
import com.intuso.housemate.proxy.ProxyRootObject;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 09/02/13
 * Time: 09:02
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyRootObject
        extends ProxyRootObject<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            GWTProxyUser, GWTProxyList<UserWrappable, GWTProxyUser>,
            GWTProxyType, GWTProxyList<TypeWrappable<?>, GWTProxyType>,
            GWTProxyDevice, GWTProxyList<DeviceWrappable, GWTProxyDevice>,
            GWTProxyRule, GWTProxyList<RuleWrappable, GWTProxyRule>,
            GWTProxyCommand, GWTProxyRootObject> {
    public GWTProxyRootObject(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                              GWTResources<?> subResources) {
        super(resources, subResources);
    }
}

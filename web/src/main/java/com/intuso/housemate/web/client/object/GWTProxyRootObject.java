package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyRootObject
        extends ProxyRootObject<
                    GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                    GWTResources<?>,
                    GWTProxyUser, GWTProxyList<UserWrappable, GWTProxyUser>,
                    GWTProxyType, GWTProxyList<TypeWrappable<?>, GWTProxyType>,
                    GWTProxyDevice, GWTProxyList<DeviceWrappable, GWTProxyDevice>,
        GWTProxyAutomation, GWTProxyList<AutomationWrappable, GWTProxyAutomation>,
                    GWTProxyCommand, GWTProxyRootObject> {
    public GWTProxyRootObject(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                              GWTResources<?> childResources) {
        super(resources, childResources);
    }
}

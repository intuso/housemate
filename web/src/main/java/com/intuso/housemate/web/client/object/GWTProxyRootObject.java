package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyRootObject
        extends ProxyRootObject<
                    GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                    GWTResources<?>,
                    GWTProxyUser, GWTProxyList<UserData, GWTProxyUser>,
                    GWTProxyType, GWTProxyList<TypeData<?>, GWTProxyType>,
                    GWTProxyDevice, GWTProxyList<DeviceData, GWTProxyDevice>,
        GWTProxyAutomation, GWTProxyList<AutomationData, GWTProxyAutomation>,
                    GWTProxyCommand, GWTProxyRootObject> {
    public GWTProxyRootObject(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                              GWTResources<?> childResources) {
        super(resources, childResources);
    }
}

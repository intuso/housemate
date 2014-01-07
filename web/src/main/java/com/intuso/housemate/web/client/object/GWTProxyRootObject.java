package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyRootObject
        extends ProxyRootObject<
                    GWTProxyUser, GWTProxyList<UserData, GWTProxyUser>,
                    GWTProxyType, GWTProxyList<TypeData<?>, GWTProxyType>,
                    GWTProxyDevice, GWTProxyList<DeviceData, GWTProxyDevice>,
        GWTProxyAutomation, GWTProxyList<AutomationData, GWTProxyAutomation>,
                    GWTProxyCommand, GWTProxyRootObject> {
    public GWTProxyRootObject(Log log,
                              Injector injector,
                              Router router) {
        super(log, injector, router);
    }
}

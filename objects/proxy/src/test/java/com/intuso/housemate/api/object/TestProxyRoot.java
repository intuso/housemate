package com.intuso.housemate.api.object;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.comms.ProxyRouterImpl;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.utilities.log.Log;
import org.junit.Ignore;

/**
 */
@Ignore
public class TestProxyRoot extends ProxyRootObject<
            SimpleProxyObject.User, SimpleProxyObject.List<UserData, SimpleProxyObject.User>,
            SimpleProxyObject.Type, SimpleProxyObject.List<TypeData<?>, SimpleProxyObject.Type>,
            SimpleProxyObject.Device, SimpleProxyObject.List<DeviceData, SimpleProxyObject.Device>,
        SimpleProxyObject.Automation, SimpleProxyObject.List<AutomationData, SimpleProxyObject.Automation>,
            SimpleProxyObject.Command, TestProxyRoot> {

    @Inject
    public TestProxyRoot(Log log, Injector injector, ProxyRouterImpl router) {
        super(log, injector, router);
        super.addChild(new SimpleProxyObject.List<TypeData<?>, SimpleProxyObject.Type>(
                log, injector, new ListData(TYPES_ID, TYPES_ID, TYPES_ID)));
        super.addChild(new SimpleProxyObject.List<DeviceData, SimpleProxyObject.Device>(
                log, injector, new ListData(DEVICES_ID, DEVICES_ID, DEVICES_ID)));
        init(null);
    }

    public void addChild(ProxyObject<?, ?, ?, ?, ?> child) {
        removeChild(child.getId());
        super.addChild(child);
        child.init(this);
    }
}

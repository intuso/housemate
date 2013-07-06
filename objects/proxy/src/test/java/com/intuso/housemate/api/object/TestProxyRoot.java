package com.intuso.housemate.api.object;

import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyResources;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import org.junit.Ignore;

/**
 */
@Ignore
public class TestProxyRoot extends ProxyRootObject<
            ProxyResources<SimpleProxyFactory.All>,
            ProxyResources<?>,
            SimpleProxyObject.User, SimpleProxyObject.List<UserData, SimpleProxyObject.User>,
            SimpleProxyObject.Type, SimpleProxyObject.List<TypeData<?>, SimpleProxyObject.Type>,
            SimpleProxyObject.Device, SimpleProxyObject.List<DeviceData, SimpleProxyObject.Device>,
        SimpleProxyObject.Automation, SimpleProxyObject.List<AutomationData, SimpleProxyObject.Automation>,
            SimpleProxyObject.Command, TestProxyRoot> {

    public TestProxyRoot(ProxyResources<SimpleProxyFactory.All> resources, ProxyResources<?> childResources) {
        super(resources, childResources);
        super.addWrapper(new SimpleProxyObject.List<TypeData<?>, SimpleProxyObject.Type>(
                SimpleProxyFactory.changeFactoryType(getResources(), new SimpleProxyFactory.Type()), childResources, new ListData(TYPES_ID, TYPES_ID, TYPES_ID)));
        super.addWrapper(new SimpleProxyObject.List<DeviceData, SimpleProxyObject.Device>(
                SimpleProxyFactory.changeFactoryType(getResources(), new SimpleProxyFactory.Device()), childResources, new ListData(DEVICES_ID, DEVICES_ID, DEVICES_ID)));
        init(null);
    }

    public void addWrapper(ProxyObject<?, ?, ?, ?, ?, ?, ?> wrapper) {
        removeWrapper(wrapper.getId());
        super.addWrapper(wrapper);
        wrapper.init(this);
    }
}

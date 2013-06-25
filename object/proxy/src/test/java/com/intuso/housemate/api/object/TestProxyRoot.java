package com.intuso.housemate.api.object;

import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
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
            SimpleProxyObject.User, SimpleProxyObject.List<UserWrappable, SimpleProxyObject.User>,
            SimpleProxyObject.Type, SimpleProxyObject.List<TypeWrappable<?>, SimpleProxyObject.Type>,
            SimpleProxyObject.Device, SimpleProxyObject.List<DeviceWrappable, SimpleProxyObject.Device>,
        SimpleProxyObject.Automation, SimpleProxyObject.List<AutomationWrappable, SimpleProxyObject.Automation>,
            SimpleProxyObject.Command, TestProxyRoot> {

    public TestProxyRoot(ProxyResources<SimpleProxyFactory.All> resources, ProxyResources<?> childResources) {
        super(resources, childResources);
        super.addWrapper(new SimpleProxyObject.List<TypeWrappable<?>, SimpleProxyObject.Type>(
                SimpleProxyFactory.changeFactoryType(getResources(), new SimpleProxyFactory.Type()), childResources, new ListWrappable(TYPES_ID, TYPES_ID, TYPES_ID)));
        super.addWrapper(new SimpleProxyObject.List<DeviceWrappable, SimpleProxyObject.Device>(
                SimpleProxyFactory.changeFactoryType(getResources(), new SimpleProxyFactory.Device()), childResources, new ListWrappable(DEVICES_ID, DEVICES_ID, DEVICES_ID)));
        init(null);
    }

    public void addWrapper(ProxyObject<?, ?, ?, ?, ?, ?, ?> wrapper) {
        removeWrapper(wrapper.getId());
        super.addWrapper(wrapper);
        wrapper.init(this);
    }
}

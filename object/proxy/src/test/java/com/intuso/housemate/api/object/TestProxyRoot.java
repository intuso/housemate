package com.intuso.housemate.api.object;

import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyResources;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import org.junit.Ignore;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:48
 * To change this template use File | Settings | File Templates.
 */
@Ignore
public class TestProxyRoot extends ProxyRootObject<
            ProxyResources<SimpleProxyFactory.All>,
            ProxyResources<?>,
            SimpleProxyObject.User, SimpleProxyObject.List<UserWrappable, SimpleProxyObject.User>,
            SimpleProxyObject.Type, SimpleProxyObject.List<TypeWrappable<?>, SimpleProxyObject.Type>,
            SimpleProxyObject.Device, SimpleProxyObject.List<DeviceWrappable, SimpleProxyObject.Device>,
            SimpleProxyObject.Rule, SimpleProxyObject.List<RuleWrappable, SimpleProxyObject.Rule>,
            SimpleProxyObject.Command, TestProxyRoot> {

    public TestProxyRoot(ProxyResources<SimpleProxyFactory.All> resources, ProxyResources<?> subResources) {
        super(resources, subResources);
        super.addWrapper(new SimpleProxyObject.List<TypeWrappable<?>, SimpleProxyObject.Type>(
                SimpleProxyFactory.changeFactoryType(getResources(), new SimpleProxyFactory.Type()), subResources, new ListWrappable(TYPES, TYPES, TYPES)));
        super.addWrapper(new SimpleProxyObject.List<DeviceWrappable, SimpleProxyObject.Device>(
                SimpleProxyFactory.changeFactoryType(getResources(), new SimpleProxyFactory.Device()), subResources, new ListWrappable(DEVICES, DEVICES, DEVICES)));
        init(null);
    }

    public void addWrapper(ProxyObject<?, ?, ?, ?, ?, ?, ?> wrapper) {
        removeWrapper(wrapper.getId());
        super.addWrapper(wrapper);
        wrapper.init(this);
    }
}

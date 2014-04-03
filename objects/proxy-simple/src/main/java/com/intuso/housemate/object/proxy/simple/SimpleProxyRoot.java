package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyRoot;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyRoot extends ProxyRoot<
        SimpleProxyApplication, SimpleProxyList<ApplicationData, SimpleProxyApplication>,
        SimpleProxyUser, SimpleProxyList<UserData, SimpleProxyUser>,
        SimpleProxyType, SimpleProxyList<TypeData<?>, SimpleProxyType>,
        SimpleProxyDevice, SimpleProxyList<DeviceData, SimpleProxyDevice>,
        SimpleProxyAutomation, SimpleProxyList<AutomationData, SimpleProxyAutomation>,
        SimpleProxyCommand, SimpleProxyRoot> {

    private final Injector injector;

    @Inject
    public SimpleProxyRoot(Log log,
                           ListenersFactory listenersFactory,
                           PropertyRepository properties,
                           Injector injector,
                           Router router) {
        super(log, listenersFactory, properties, router);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getInstance(new Key<HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).create(data);
    }
}

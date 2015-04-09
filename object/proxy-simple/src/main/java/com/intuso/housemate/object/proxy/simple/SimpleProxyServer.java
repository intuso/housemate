package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.server.ServerData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyServer;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyServer extends ProxyServer<
        SimpleProxyApplication, SimpleProxyList<ApplicationData, SimpleProxyApplication>,
        SimpleProxyAutomation, SimpleProxyList<AutomationData, SimpleProxyAutomation>,
        SimpleProxyDevice, SimpleProxyList<DeviceData, SimpleProxyDevice>,
        SimpleProxyHardware, SimpleProxyList<HardwareData, SimpleProxyHardware>,
        SimpleProxyType, SimpleProxyList<TypeData<?>, SimpleProxyType>,
        SimpleProxyUser, SimpleProxyList<UserData, SimpleProxyUser>,
        SimpleProxyCommand, SimpleProxyServer> {

    private final Injector injector;

    @Inject
    public SimpleProxyServer(Log log,
                             ListenersFactory listenersFactory,
                             Injector injector,
                             ServerData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getInstance(new Key<HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).create(data);
    }
}

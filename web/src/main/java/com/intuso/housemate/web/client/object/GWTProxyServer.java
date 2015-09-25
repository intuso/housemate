package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyServer;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyServer extends ProxyServer<
            GWTProxyApplication, GWTProxyList<ApplicationData, GWTProxyApplication>,
            GWTProxyAutomation, GWTProxyList<AutomationData, GWTProxyAutomation>,
            GWTProxyDevice, GWTProxyList<DeviceData, GWTProxyDevice>,
            GWTProxyHardware, GWTProxyList<HardwareData, GWTProxyHardware>,
            GWTProxyType, GWTProxyList<TypeData<?>, GWTProxyType>,
            GWTProxyUser, GWTProxyList<UserData, GWTProxyUser>,
            GWTProxyCommand, GWTProxyServer> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyServer(Log log,
                          ListenersFactory listenersFactory,
                          GWTGinjector injector,
                          @Assisted ServerData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }
}

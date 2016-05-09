package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.*;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyServer;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyServer extends ProxyServer<
            GWTProxyApplication, GWTProxyList<ApplicationData, GWTProxyApplication>,
            GWTProxyAutomation, GWTProxyList<Automation.Data, GWTProxyAutomation>,
            GWTProxyDevice, GWTProxyList<Device.Data, GWTProxyDevice>,
            GWTProxyHardware, GWTProxyList<Hardware.Data, GWTProxyHardware>,
            GWTProxyType, GWTProxyList<Type.Data<?>, GWTProxyType>,
            GWTProxyUser, GWTProxyList<User.Data, GWTProxyUser>,
            GWTProxyCommand, GWTProxyServer> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyServer(Logger logger,
                          ListenersFactory listenersFactory,
                          GWTGinjector injector,
                          @Assisted Data data) {
        super(logger, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChild(Object.Data data) {
        return injector.getObjectFactory().create(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}

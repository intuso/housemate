package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.realclient.RealClientData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyRealClient;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyRealClient extends ProxyRealClient<
            GWTProxyApplication, GWTProxyList<ApplicationData, GWTProxyApplication>,
            GWTProxyAutomation, GWTProxyList<AutomationData, GWTProxyAutomation>,
            GWTProxyDevice, GWTProxyList<DeviceData, GWTProxyDevice>,
            GWTProxyHardware, GWTProxyList<HardwareData, GWTProxyHardware>,
            GWTProxyType, GWTProxyList<TypeData<?>, GWTProxyType>,
            GWTProxyUser, GWTProxyList<UserData, GWTProxyUser>,
            GWTProxyCommand, GWTProxyRealClient> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyRealClient(Log log,
                              ListenersFactory listenersFactory,
                              GWTGinjector injector,
                              @Assisted RealClientData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }
}

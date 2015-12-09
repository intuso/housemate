package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyServer;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyServer extends ProxyServer<
        AndroidProxyApplication, AndroidProxyList<ApplicationData, AndroidProxyApplication>,
        AndroidProxyAutomation, AndroidProxyList<AutomationData, AndroidProxyAutomation>,
        AndroidProxyDevice, AndroidProxyList<DeviceData, AndroidProxyDevice>,
        AndroidProxyHardware, AndroidProxyList<HardwareData, AndroidProxyHardware>,
        AndroidProxyType, AndroidProxyList<TypeData<HousemateData<?>>, AndroidProxyType>,
        AndroidProxyUser, AndroidProxyList<UserData, AndroidProxyUser>,
        AndroidProxyCommand, AndroidProxyServer> {

    private final AndroidProxyFactory factory;

    /**
     * @param log    {@inheritDoc}
     */
    public AndroidProxyServer(Logger logger, ListenersFactory listenersFactory, ServerData data, AndroidProxyFactory factory) {
        super(logger, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected ProxyObject createChildInstance(HousemateData<?> data) {
        return factory.create(data);
    }
}

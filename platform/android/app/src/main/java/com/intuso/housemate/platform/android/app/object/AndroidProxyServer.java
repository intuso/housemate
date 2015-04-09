package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.api.object.HousemateData;
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
    public AndroidProxyServer(Log log, ListenersFactory listenersFactory, ServerData data, AndroidProxyFactory factory) {
        super(log, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected ProxyObject createChildInstance(HousemateData<?> data) {
        return factory.create(data);
    }
}

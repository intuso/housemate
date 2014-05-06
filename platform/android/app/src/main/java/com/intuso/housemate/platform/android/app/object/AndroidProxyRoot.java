package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateData;
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
 * Date: 24/02/14
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyRoot extends ProxyRoot<
        AndroidProxyApplication, AndroidProxyList<ApplicationData, AndroidProxyApplication>,
        AndroidProxyUser, AndroidProxyList<UserData, AndroidProxyUser>,
        AndroidProxyType, AndroidProxyList<TypeData<HousemateData<?>>, AndroidProxyType>,
        AndroidProxyDevice, AndroidProxyList<DeviceData, AndroidProxyDevice>,
        AndroidProxyAutomation, AndroidProxyList<AutomationData, AndroidProxyAutomation>,
        AndroidProxyCommand, AndroidProxyRoot> {

    private final AndroidProxyFactory factory;

    /**
     * @param log    {@inheritDoc}
     * @param router The router to connect through
     */
    public AndroidProxyRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router) {
        super(log, listenersFactory, properties, router);
        this.factory = new AndroidProxyFactory(log, listenersFactory);
    }

    @Override
    protected ProxyObject createChildInstance(HousemateData<?> data) {
        return factory.create(data);
    }
}

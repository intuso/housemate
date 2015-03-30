package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.realclient.RealClientData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyRealClient;
import com.intuso.housemate.object.proxy.ProxyRoot;
import com.intuso.housemate.object.proxy.simple.comms.ProxyRouterImpl;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.junit.Ignore;

/**
 */
@Ignore
public class TestProxyRoot extends ProxyRoot<
        SimpleProxyRealClient, SimpleProxyList<RealClientData, SimpleProxyRealClient>,
        TestProxyRoot> {

    private final Injector injector;

    @Inject
    public TestProxyRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Injector injector, ProxyRouterImpl router) {
        super(log, listenersFactory, properties, router);
        try {
            distributeMessage(new Message<>(new String[] {""}, Root.SERVER_CONNECTION_STATUS_TYPE, ServerConnectionStatus.ConnectedToServer));
            distributeMessage(new Message<>(new String[] {""}, Root.APPLICATION_STATUS_TYPE, ApplicationStatus.AllowInstances));
            distributeMessage(new Message<>(new String[] {""}, Root.APPLICATION_INSTANCE_STATUS_TYPE, ApplicationInstanceStatus.Allowed));
        } catch (HousemateException e) {
            e.printStackTrace();
        }
        this.injector = injector;
        super.addChild(new SimpleProxyList<TypeData<?>, SimpleProxyType>(
                log, listenersFactory, injector, new ListData(ProxyRealClient.TYPES_ID, ProxyRealClient.TYPES_ID, ProxyRealClient.TYPES_ID)));
        super.addChild(new SimpleProxyList<DeviceData, SimpleProxyDevice>(
                log, listenersFactory, injector, new ListData(ProxyRealClient.DEVICES_ID, ProxyRealClient.DEVICES_ID, ProxyRealClient.DEVICES_ID)));
        init(null);
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getInstance(new Key<HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).create(data);
    }

    public void addChild(ProxyObject<?, ?, ?, ?, ?> child) {
        removeChild(child.getId());
        super.addChild(child);
        child.init(this);
    }
}

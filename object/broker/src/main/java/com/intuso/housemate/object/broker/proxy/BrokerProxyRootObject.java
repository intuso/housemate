package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.root.RootWrappable;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 */
public class BrokerProxyRootObject
        extends BrokerProxyObject<RootWrappable, HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>,
            BrokerProxyRootObject, RootListener<? super BrokerProxyRootObject>>
        implements Root<BrokerProxyRootObject, RootListener<? super BrokerProxyRootObject>> {

    private BrokerProxyList<TypeWrappable<?>, BrokerProxyType> types;
    private BrokerProxyList<DeviceWrappable, BrokerProxyDevice> devices;

    public BrokerProxyRootObject(BrokerProxyResources<BrokerProxyFactory.All> resources) {
        super(resources, new RootWrappable());
        types = new BrokerProxyList<TypeWrappable<?>, BrokerProxyType>(
                BrokerProxyFactory.changeFactoryType(resources, new BrokerProxyFactory.Type()), new ListWrappable(TYPES_ID, TYPES_ID, "Proxied types"));
        devices = new BrokerProxyList<DeviceWrappable, BrokerProxyDevice>(
                BrokerProxyFactory.changeFactoryType(resources, new BrokerProxyFactory.Device()), new ListWrappable<DeviceWrappable>(DEVICES_ID, DEVICES_ID, "Proxied devices"));

        addWrapper(types);
        addWrapper(devices);

        init(null);
    }

    @Override
    public ConnectionStatus getStatus() {
        return ConnectionStatus.Authenticated;
    }

    @Override
    public String getConnectionId() {
        return null;
    }

    @Override
    public void login(AuthenticationMethod method) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void logout() {
        throw new HousemateRuntimeException("Cannot disconnect this type of root object");
    }

    @Override
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
        throw new HousemateRuntimeException("This root object is not intended to have listeners on its child objects");
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        throw new HousemateRuntimeException("Whatever");
    }

    public BrokerProxyList<TypeWrappable<?>, BrokerProxyType> getTypes() {
        return types;
    }

    public BrokerProxyList<DeviceWrappable, BrokerProxyDevice> getDevices() {
        return devices;
    }
}

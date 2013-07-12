package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.utilities.listener.ListenerRegistration;

public class BrokerProxyRootObject
        extends BrokerProxyObject<RootData, HousemateData<?>, BrokerProxyObject<?, ?, ?, ?, ?>,
            BrokerProxyRootObject, RootListener<? super BrokerProxyRootObject>>
        implements Root<BrokerProxyRootObject> {

    private BrokerProxyList<TypeData<?>, BrokerProxyType> types;
    private BrokerProxyList<DeviceData, BrokerProxyDevice> devices;

    /**
     * @param resources {@inheritDoc}
     */
    public BrokerProxyRootObject(BrokerProxyResources<BrokerProxyFactory.All> resources) {
        super(resources, new RootData());
        types = new BrokerProxyList<TypeData<?>, BrokerProxyType>(
                BrokerProxyFactory.changeFactoryType(resources, new BrokerProxyFactory.Type()), new ListData(TYPES_ID, TYPES_ID, "Proxied types"));
        devices = new BrokerProxyList<DeviceData, BrokerProxyDevice>(
                BrokerProxyFactory.changeFactoryType(resources, new BrokerProxyFactory.Device()), new ListData<DeviceData>(DEVICES_ID, DEVICES_ID, "Proxied devices"));

        addChild(types);
        addChild(devices);

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

    /**
     * Gets proxy types for the remote client's types
     * @return proxy types for the remove client's types
     */
    public BrokerProxyList<TypeData<?>, BrokerProxyType> getTypes() {
        return types;
    }

    /**
     * Gets proxy devices for the remote client's devices
     * @return proxy devices for the remote client's devices
     */
    public BrokerProxyList<DeviceData, BrokerProxyDevice> getDevices() {
        return devices;
    }
}

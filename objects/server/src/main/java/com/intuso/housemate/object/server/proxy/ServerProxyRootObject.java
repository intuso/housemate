package com.intuso.housemate.object.server.proxy;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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

@Singleton
public class ServerProxyRootObject
        extends ServerProxyObject<RootData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>,
        ServerProxyRootObject, RootListener<? super ServerProxyRootObject>>
        implements Root<ServerProxyRootObject> {

    private ServerProxyList<TypeData<?>, ServerProxyType> types;
    private ServerProxyList<DeviceData, ServerProxyDevice> devices;

    /**
     * @param resources {@inheritDoc}
     */
    @Inject
    public ServerProxyRootObject(ServerProxyResources<ServerProxyFactory.All> resources) {
        super(resources, new RootData());
        types = new ServerProxyList<TypeData<?>, ServerProxyType>(
                ServerProxyFactory.changeFactoryType(resources, new ServerProxyFactory.Type()), new ListData(TYPES_ID, TYPES_ID, "Proxied types"));
        devices = new ServerProxyList<DeviceData, ServerProxyDevice>(
                ServerProxyFactory.changeFactoryType(resources, new ServerProxyFactory.Device()), new ListData<DeviceData>(DEVICES_ID, DEVICES_ID, "Proxied devices"));

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
    public ServerProxyList<TypeData<?>, ServerProxyType> getTypes() {
        return types;
    }

    /**
     * Gets proxy devices for the remote client's devices
     * @return proxy devices for the remote client's devices
     */
    public ServerProxyList<DeviceData, ServerProxyDevice> getDevices() {
        return devices;
    }
}

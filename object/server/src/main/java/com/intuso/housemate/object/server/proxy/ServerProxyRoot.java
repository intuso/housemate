package com.intuso.housemate.object.server.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerProxyRoot
        extends ServerProxyObject<RootData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyRoot, RootListener<? super ServerProxyRoot>>
        implements Root<ServerProxyRoot> {

    private ServerProxyList<HardwareData, ServerProxyHardware> hardwares;
    private ServerProxyList<TypeData<?>, ServerProxyType> types;
    private ServerProxyList<DeviceData, ServerProxyDevice> devices;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     */
    @Inject
    public ServerProxyRoot(Log log, ListenersFactory listenersFactory, Injector injector, ServerProxyList<TypeData<?>, ServerProxyType> types) {
        super(log, listenersFactory, injector, new RootData());

        hardwares = new ServerProxyList<HardwareData, ServerProxyHardware>(log, listenersFactory, injector, new ListData<HardwareData>(HARDWARES_ID, HARDWARES_ID, "Connected hardware"));
        this.types = types;
        devices = new ServerProxyList<DeviceData, ServerProxyDevice>(log, listenersFactory, injector, new ListData<DeviceData>(DEVICES_ID, DEVICES_ID, "Proxied devices"));

        addChild(hardwares);
        addChild(types);
        addChild(devices);

        init(null);
    }

    @Override
    public ApplicationStatus getApplicationStatus() {
        return ApplicationStatus.AllowInstances;
    }

    @Override
    public ApplicationInstanceStatus getApplicationInstanceStatus() {
        return ApplicationInstanceStatus.Allowed;
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void unregister() {
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

    public ServerProxyList<HardwareData, ServerProxyHardware> getHardwares() {
        return hardwares;
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

    @Override
    protected void copyValues(HousemateData<?> data) {
        // do nothing
    }
}

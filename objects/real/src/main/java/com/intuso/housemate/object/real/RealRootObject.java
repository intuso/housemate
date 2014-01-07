package com.intuso.housemate.object.real;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.message.AuthenticationResponse;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.root.real.RealRoot;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.List;

public class RealRootObject
        extends RealObject<RootData, HousemateData<?>, RealObject<?, ? extends HousemateData<?>, ?, ?>, RootListener<? super RealRootObject>>
        implements RealRoot<RealType<?, ?, ?>, RealList<TypeData<?>, RealType<?, ?, ?>>, RealDevice,
                            RealList<DeviceData, RealDevice>, RealRootObject>, ConnectionStatusChangeListener {

    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final RealList<DeviceData, RealDevice> devices;

    private final Router.Registration routerRegistration;
    private final ConnectionManager connectionManager;

    private boolean resend = false;

    /**
     * @param log {@inheritDoc}
     */
    public RealRootObject(Log log, Router router) {
        this(log, router, new RealList<TypeData<?>, RealType<?, ?, ?>>(log, TYPES_ID, TYPES_ID, "Defined types"),
            new RealList<DeviceData, RealDevice>(log, DEVICES_ID, DEVICES_ID, "Defined devices"));
    }

    @Inject
    public RealRootObject(Log log, Router router, RealList<TypeData<?>, RealType<?, ?, ?>> types,
            RealList<DeviceData, RealDevice> devices) {
        super(log, new RootData());

        routerRegistration = router.registerReceiver(this);
        connectionManager = new ConnectionManager(routerRegistration, ConnectionType.Real, ConnectionStatus.Unauthenticated);

        this.types = types;
        this.devices = devices;

        init(null);

        addChild(types);
        types.init(this);
        addChild(devices);
        devices.init(this);

    }

    @Override
    public ConnectionStatus getStatus() {
        return connectionManager.getStatus();
    }

    @Override
    public String getConnectionId() {
        return connectionManager.getConnectionId();
    }

    @Override
    public void login(AuthenticationMethod method) {
        connectionManager.login(method);
    }

    @Override
    public void logout() {
        connectionManager.logout();
        routerRegistration.remove();
    }

    @Override
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
        throw new HousemateRuntimeException("This root object is not intended to have listeners on its child objects");
    }

    @Override
    public void sendMessage(Message<?> message) {
        routerRegistration.sendMessage(message);
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(connectionManager.addStatusChangeListener(this));
        result.add(addMessageListener(CONNECTION_RESPONSE_TYPE, new Receiver<AuthenticationResponse>() {
            @Override
            public void messageReceived(Message<AuthenticationResponse> message) throws HousemateException {
                connectionManager.authenticationResponseReceived(message.getPayload());
            }
        }));
        result.add(addMessageListener(STATUS_TYPE, new Receiver<ConnectionStatus>() {
            @Override
            public void messageReceived(Message<ConnectionStatus> message) throws HousemateException {
                connectionManager.routerStatusChanged(message.getPayload());
            }
        }));
        return result;
    }

    @Override
    public final RealList<TypeData<?>, RealType<?, ?, ?>> getTypes() {
        return types;
    }

    @Override
    public final void addType(RealType<?, ?, ?> type) {
        types.add(type);
    }

    @Override
    public final void removeType(String name) {
        types.remove(name);
    }

    @Override
    public final RealList<DeviceData, RealDevice> getDevices() {
        return devices;
    }

    @Override
    public final void addDevice(RealDevice device) {
        devices.add(device);
    }

    @Override
    public final void removeDevice(String name) {
        devices.remove(name);
    }

    @Override
    public void connectionStatusChanged(ConnectionStatus status) {
        for(RootListener<? super RealRootObject> listener : getObjectListeners())
            listener.connectionStatusChanged(this, status);
        if(status == ConnectionStatus.Authenticated && resend) {
            types.resendElements();
            devices.resendElements();
        }
    }

    @Override
    public void newServerInstance() {
        for(RootListener<? super RealRootObject> listener : getObjectListeners())
            listener.newServerInstance(this);
        resend = true;
    }
}
